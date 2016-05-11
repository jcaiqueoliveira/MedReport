package dev.tcc.caique.medreport.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Invite;
import dev.tcc.caique.medreport.models.Singleton;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.DialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends Fragment {
    public InviteFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.noItem)
    TextView noItem;
    FirebaseRecyclerAdapter<Invite, ViewHolderInvite> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invite, container, false);
        ButterKnife.bind(this, v);
        final Firebase ref = new Firebase(Constants.BASE_URL);
        final Firebase ref2 = new Firebase(Constants.BASE_URL + "invites/" + ref.getAuth().getUid());
        if (Singleton.getInstance().getType().equals("1"))
            ((MainActivity) getActivity()).fab.show();
        else
            ((MainActivity) getActivity()).fab.hide();

        recyclerView = (RecyclerView) v.findViewById(R.id.inviteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        try {
            adapter = new FirebaseRecyclerAdapter<Invite, ViewHolderInvite>(Invite.class,
                    R.layout.layout_invite_card_list,
                    ViewHolderInvite.class,
                    ref2) {
                @Override
                protected void populateViewHolder(ViewHolderInvite viewHolderInvite, final Invite inviter, int i) {
                    Log.e("REF",ref2.getKey());
                    viewHolderInvite.nameInviter.setText(inviter.getName());
                    viewHolderInvite.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //todo requisition here
                            final String chatValue = ref.push().getKey();
                            final Map<String, String> map = new HashMap<>();
                            map.put("email", inviter.getEmail());
                            map.put("chat", chatValue);
                            String stackId = ref.push().getKey();
                            map.put("stackId", stackId);
                            ref.child("friends").child(ref.getAuth().getUid()).child(stackId).setValue(map, new Firebase.CompletionListener() {//adicionando a quem aceitou o contive
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError == null) {
                                        Singleton.getInstance().getFriends().add(inviter.getEmail());
                                        map.clear();
                                        map.put("email", (String) ref.getAuth().getProviderData().get("email"));
                                        map.put("chat", chatValue);
                                        final String stackId2 = ref.push().getKey();
                                        map.put("stackId", stackId2);
                                        Query query = ref.child("users").orderByChild("email").equalTo(inviter.getEmail());
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Log.e("DATA", dataSnapshot.getValue().toString());
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    ref.child("friends").child(ds.getKey()).child(stackId2).setValue(map, new Firebase.CompletionListener() {//adicionando a quem enviou o convite
                                                        @Override
                                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                            if (firebaseError == null) {
                                                                ref.child("users").child(ref.getAuth().getUid()).child("invites").child(inviter.getStackId()).removeValue(new Firebase.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                                        if (firebaseError == null) {
                                                                            Log.i("removido", "sucesso");
                                                                        } else {
                                                                            Log.i("error", firebaseError.getMessage());
                                                                        }
                                                                    }
                                                                });
                                                                Toast.makeText(getActivity(), "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                                                                ref.child("invites").child(ref.getAuth().getUid()).child(inviter.getStackId()).removeValue();
                                                            } else {
                                                                Log.i("error", firebaseError.getMessage());
                                                            }
                                                        }
                                                    });
                                                }

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });

                                    } else {
                                        Log.i("error", firebaseError.getMessage());
                                    }
                                }
                            });
                        }
                    });

                    viewHolderInvite.exclude.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ref.child("invites").child(ref.getAuth().getUid()).child(inviter.getStackId()).removeValue(new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError == null) {
                                        Toast.makeText(getActivity(), "Não adicionado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Erro ao excluir o convite. Tente novamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            };
            recyclerView.setAdapter(adapter);
            updateUI();
        } catch (Exception e) {
            Log.e("error1", e.getMessage());
        }

        ((MainActivity) getActivity()).fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.inviteFriend(getActivity());
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.INVITE);
        super.onResume();
    }

    public static class ViewHolderInvite extends RecyclerView.ViewHolder {
        public ImageView accept, exclude;
        public TextView nameInviter;
        public CircleImageView thumbnal;

        public ViewHolderInvite(View v) {
            super(v);
            accept = (ImageView) v.findViewById(R.id.accept);
            exclude = (ImageView) v.findViewById(R.id.recuse);
            nameInviter = (TextView) v.findViewById(R.id.nameInviter);
            thumbnal = (CircleImageView) v.findViewById(R.id.thumbnail);
        }
    }

    public void updateUI() {
        if (adapter.getItemCount() == 0) {
            noItem.setVisibility(View.VISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
        }
    }
}

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

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.HashMap;
import java.util.Map;

import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Invite;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.DialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends Fragment {
    public InviteFragment() {
        // Required empty public constructor
    }

    FirebaseRecyclerAdapter<Invite, ViewHolderInvite> adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invite, container, false);

        final Firebase ref = new Firebase("https://medreportapp.firebaseio.com/");
        final Firebase ref2 = new Firebase("https://medreportapp.firebaseio.com/users/" + ref.getAuth().getUid() + "/invites");
        ((MainActivity) getActivity()).fab.show();
        recyclerView = (RecyclerView) v.findViewById(R.id.inviteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            adapter = new FirebaseRecyclerAdapter<Invite, ViewHolderInvite>(Invite.class,
                    R.layout.layout_invite_card_list,
                    ViewHolderInvite.class,
                    ref2) {
                @Override
                protected void populateViewHolder(ViewHolderInvite viewHolderInvite, final Invite inviter, int i) {
                    viewHolderInvite.nameInviter.setText(inviter.toString());
                    viewHolderInvite.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //todo requisition here
                            final String chatValue = ref.push().getKey();
                            final Map<String, String> map = new HashMap<>();
                            map.put("stackId", inviter.getUser());
                            map.put("chat", chatValue);
                            ref.child("users").child(ref.getAuth().getUid()).child("friends").push().setValue(map, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError == null) {
                                        map.clear();
                                        map.put("stackId", ref.getAuth().getUid());
                                        map.put("chat", chatValue);
                                        ref.child("users").child(inviter.getUser()).child("friends").push().setValue(map, new Firebase.CompletionListener() {
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
                                                } else {
                                                    Log.i("error", firebaseError.getMessage());
                                                }
                                            }
                                        });
                                    } else {
                                        Log.i("error", firebaseError.getMessage());
                                    }
                                }
                            });
                            //ref2.child("users").child(ref.getAuth().getUid()).child("invites").child("" + inviter.getStackId()).child(inviter.getUid());
                            ref2.removeValue(new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    if (firebaseError != null) {
                                    } else {
                                        ref.child("users/" + ref.getAuth().getUid() + "/friends/");
                                        // ref.setValue(new );
                                    }
                                }
                            });
                        }
                    });

                    viewHolderInvite.exclude.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            };
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
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

        public ViewHolderInvite(View v) {
            super(v);
            accept = (ImageView) v.findViewById(R.id.accept);
            exclude = (ImageView) v.findViewById(R.id.recuse);
            nameInviter = (TextView) v.findViewById(R.id.nameInviter);
        }
    }
}

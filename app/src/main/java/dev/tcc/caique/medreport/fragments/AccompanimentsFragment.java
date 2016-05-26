package dev.tcc.caique.medreport.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.tcc.caique.medreport.R;
import dev.tcc.caique.medreport.activities.ChatActivity;
import dev.tcc.caique.medreport.activities.MainActivity;
import dev.tcc.caique.medreport.models.Accompaniments;
import dev.tcc.caique.medreport.utils.Constants;
import dev.tcc.caique.medreport.utils.DialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccompanimentsFragment extends Fragment {
    @Bind(R.id.accompanimentsList)
    RecyclerView accompanimentsList;
    FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments> adapter;
    @Bind(R.id.noItem)
    TextView noItem;

    public AccompanimentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accompaniments, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity) getActivity()).fab.hide();
        accompanimentsList.setHasFixedSize(true);
        accompanimentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Firebase ref = new Firebase(Constants.BASE_URL);
        final Firebase ref2 = new Firebase(Constants.BASE_URL + "friends/" + ref.getAuth().getUid());
        final Firebase ref3 = new Firebase(Constants.BASE_URL + "users");
        try {
            adapter = new FirebaseRecyclerAdapter<Accompaniments, ViewHolderAccompaniments>(Accompaniments.class, R.layout.layout_accompaniments, ViewHolderAccompaniments.class, ref2) {
                @Override
                protected void populateViewHolder(final ViewHolderAccompaniments viewHolderAccompaniments, final Accompaniments accompaniments, int i) {
                    noItem.setVisibility(View.GONE);
                    final String[] url = {null};
                    Query queryRef = ref3.orderByChild("email").equalTo(accompaniments.getEmail());
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                Log.e("dados", ds.toString());
                                viewHolderAccompaniments.namePerson.setText((String) ds.child("name").getValue());
                                url[0] = (String) ds.child("profile").child("profileUrl").getValue();
                                if (url[0] != null) {
                                    Glide.with(getActivity()).load(url[0]).into(viewHolderAccompaniments.thumbnail);
                                }
                                viewHolderAccompaniments.informations.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        if (ds.child("type").getValue().toString().equals(Constants.TYPE_MEDICAL)) {
                                            builder.setView(getProfileMedicalInfo(ds, url[0]));
                                        } else {
                                            builder.setView(getProfilePacientInfo(ds, url[0]));
                                        }
                                        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.i("error search", firebaseError.getMessage());
                        }
                    });

                    viewHolderAccompaniments.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            Bundle b = new Bundle();
                            b.putString("SALA", accompaniments.getChat());
                            b.putString("USUARIO", viewHolderAccompaniments.namePerson.getText().toString());
                            b.putString("FOTO", url[0]);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    viewHolderAccompaniments.informations.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    viewHolderAccompaniments.accompanimentRow.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Log.d("OnLongClick","Paciente: "+viewHolderAccompaniments.namePerson.getText().toString());
                            DialogUtils.deleteAccompanimentDialog(getActivity(),accompaniments.getStackId(),accompaniments.getChat());
                            //get user id, then chat id with user id and then delete friends with usr id and chat with chat id

                            return true;
                        }
                    });
                }
            };
            accompanimentsList.setAdapter(adapter);
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).navigationView.setCheckedItem(Constants.ACCOMPANIMENTS);
        super.onResume();
    }

    public static class ViewHolderAccompaniments extends RecyclerView.ViewHolder {
        public LinearLayout accompanimentRow;
        public CircleImageView thumbnail;
        public TextView namePerson;
        public ImageView informations;
        public View view;

        public interface OnItemLongClickListener {
            public boolean onItemLongClicked(int position);
        }

        public ViewHolderAccompaniments(View v) {
            super(v);
            this.view = v;
            thumbnail = (CircleImageView) v.findViewById(R.id.thumbnail);
            namePerson = (TextView) v.findViewById(R.id.nameAccompanimentList);
            informations = (ImageView) v.findViewById(R.id.informations);
            accompanimentRow = (LinearLayout) v.findViewById(R.id.accompanimentRow);
        }
    }

    public void updateUI() {
        if (adapter.getItemCount() == 0) {
            noItem.setVisibility(View.VISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
        }
    }

    private View getProfileMedicalInfo(DataSnapshot ds, String url) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_profile_medical, null);
        CircleImageView imageProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        EditText specialization = (EditText) view.findViewById(R.id.specialization);
        EditText crm = (EditText) view.findViewById(R.id.crm);
        AppCompatEditText name = (AppCompatEditText) view.findViewById(R.id.nameMedical);
        name.setText(ds.child("name").getValue().toString());
        name.setEnabled(false);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(getActivity()).load(url).into(imageProfile);
        }
        if (!TextUtils.isEmpty((String) ds.child("profile").child("specialization").getValue()))
            specialization.setText((String) ds.child("profile").child("specialization").getValue());
        else
            specialization.setVisibility(View.GONE);

        if (!TextUtils.isEmpty((String) ds.child("profile").child("crm").getValue()))
            crm.setText((String) ds.child("profile").child("crm").getValue());
        else
            crm.setVisibility(View.GONE);

        specialization.setEnabled(false);
        crm.setEnabled(false);
        return view;
    }

    private View getProfilePacientInfo(DataSnapshot ds, String url) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_profile_pacient, null);
        CircleImageView imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        AppCompatEditText edtName = (AppCompatEditText) view.findViewById(R.id.edtName);
        AppCompatEditText edtAge = (AppCompatEditText) view.findViewById(R.id.edtAge);
        AppCompatEditText edtGender = (AppCompatEditText) view.findViewById(R.id.edtGender);
        AppCompatEditText edtHeight = (AppCompatEditText) view.findViewById(R.id.edtHeight);
        AppCompatEditText weight = (AppCompatEditText) view.findViewById(R.id.edtWeight);
        AppCompatEditText edtProblem1 = (AppCompatEditText) view.findViewById(R.id.edtProblem1);
        AppCompatEditText edtProblem2 = (AppCompatEditText) view.findViewById(R.id.edtProblem2);
        AppCompatEditText edtProblem3 = (AppCompatEditText) view.findViewById(R.id.edtProblem3);
        if (!TextUtils.isEmpty(url))
            Glide.with(getActivity()).load(url).into(imgProfile);
        edtName.setText(ds.child("name").getValue().toString());
        if (!TextUtils.isEmpty(ds.child("profile").child("age").getValue().toString()))
            edtAge.setText(ds.child("profile").child("age").getValue().toString());
            else
            edtAge.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("gender").getValue().toString()))
            edtGender.setText(ds.child("profile").child("gender").getValue().toString());
        else
            edtGender.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("stature").getValue().toString()))
            edtHeight.setText(ds.child("profile").child("stature").getValue().toString());
        else
            edtHeight.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("weight").getValue().toString()))
            weight.setText(ds.child("profile").child("weight").getValue().toString());
        else
            weight.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("healthProblem").getValue().toString()))
            edtProblem1.setText(ds.child("profile").child("healthProblem").getValue().toString());
        else
            edtProblem1.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("allergy").getValue().toString()))
            edtProblem2.setText(ds.child("profile").child("allergy").getValue().toString());
        else
            edtProblem2.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ds.child("profile").child("drugAllergy").getValue().toString()))
            edtProblem3.setText(ds.child("profile").child("drugAllergy").getValue().toString());
        else
            edtProblem3.setVisibility(View.GONE);

        edtName.setEnabled(false);
        edtAge.setEnabled(false);
        edtGender.setEnabled(false);
        edtHeight.setEnabled(false);
        weight.setEnabled(false);
        edtProblem1.setEnabled(false);
        edtProblem2.setEnabled(false);
        edtProblem3.setEnabled(false);
        return view;
        }
    }

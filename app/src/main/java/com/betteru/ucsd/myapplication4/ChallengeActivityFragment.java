package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */


public class ChallengeActivityFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener{
    static ChallengeModel data;
    View view;
    public final static int EDITDIALOG_FRAGMENT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        Log.d("DATA", Integer.toString(data.activitiesIcon.size()));
        view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        loadChallengeName();
        loadChallengeDate();
        loadChallengeParticipants();
        loadChallengeActivities();
        loadChallengeDateButton();
        loadChallengeNameButton();
        return view;
    }

    public void loadChallengeName() {
        TextView name = (TextView) view.findViewById(R.id.textView_challengeName);
        name.setText(data.title);
    }
    public void loadChallengeDate(){
        TextView date = (TextView) view.findViewById(R.id.textView_challengeDate);
        date.setText(data.date.format(data.formatter));
    }
    public void loadChallengeParticipants() {
        //set GridView
        GridView gridViewParticipants = (GridView) view.findViewById(R.id.gridview_challenge_participants);
        ChallengeActivityAdapter adapterPar = new ChallengeActivityAdapter(this.getActivity(), data.participants, data.participantsIcon);
        gridViewParticipants.setAdapter(adapterPar);
    }
    public void loadChallengeActivities(){
        //set GridView
        GridView gridViewActivities = (GridView) view.findViewById(R.id.gridView_challenge_activities);
        ChallengeActivityAdapter adapterAct =new ChallengeActivityAdapter(this.getActivity(), data.activities, data.activitiesIcon);
        gridViewActivities.setAdapter(adapterAct);
    }

    private void loadChallengeDateButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeDate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ChallengeActivityFragment.this,
                        data.date.getYear(),
                        data.date.getMonthValue()-1,
                        data.date.getDayOfMonth()
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    public void loadChallengeNameButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = EditChallengeNameDialogFragment.newInstance(data.title);
                newFragment.setTargetFragment(ChallengeActivityFragment.this, EDITDIALOG_FRAGMENT);
                newFragment.show(getFragmentManager(), "challengeNameEditDialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        data.date = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        loadChallengeDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDITDIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String title = bundle.getString("challengeName");
                    this.data.title = title;
                    loadChallengeName();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                }
                break;
        }
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            datePickerFragment = new DatePickerFragment1();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return datePickerFragment;
        }
    }

    public static class EditChallengeNameDialogFragment extends DialogFragment
            implements DialogInterface.OnDismissListener{

        public static EditChallengeNameDialogFragment newInstance(String title) {
            EditChallengeNameDialogFragment f = new EditChallengeNameDialogFragment();
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString("challengeName", title);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setTitle("Challenge Name");
            final View dialogView = inflater.inflate(R.layout.dialog_challenge_name, null);
            builder.setView(dialogView);
            EditText edit = (EditText) dialogView.findViewById(R.id.editText_challenge_name);
            String challengeName = getArguments().getString("challengeName");
            edit.setText(challengeName);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EditText edit = (EditText) dialogView.findViewById(R.id.editText_challenge_name);
                    String title = edit.getText().toString();
                    Intent i = new Intent().putExtra("challengeName", title);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                    //EditChallengeNameDialogFragment.this.loadChallengeName();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    EditChallengeNameDialogFragment.this.getDialog().cancel();
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}

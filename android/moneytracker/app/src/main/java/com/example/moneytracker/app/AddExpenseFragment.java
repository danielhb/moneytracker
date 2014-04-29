package com.example.moneytracker.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.moneytracker.app.model.Expense;
import com.example.moneytracker.app.model.ExpenseList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddExpenseFragment.OnExpenseAddedListener} interface
 * to handle interaction events.
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static ArrayList<String> tagsAutoComplete = new ArrayList<String>();

    // Calendar object to manage the date picker date
    final Calendar dateExpenseCalendar = Calendar.getInstance();

    // reference to the tagsTextView
    MultiAutoCompleteTextView tagsTextView;

    private OnExpenseAddedListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_add_expense, container, false);

         /*
         * Inserting a datepicker to select the date in the EditText
         */
        final EditText dateEditText = (EditText)fragmentView.findViewById(R.id.editText);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    dateExpenseCalendar.set(Calendar.YEAR, year);
                    dateExpenseCalendar.set(Calendar.MONTH, monthOfYear);
                    dateExpenseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = Expense.dateFormat;
                    dateEditText.setText(sdf.format(dateExpenseCalendar.getTime()));
                }
            };

            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, dateExpenseCalendar
                        .get(Calendar.YEAR), dateExpenseCalendar.get(Calendar.MONTH),
                        dateExpenseCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*
         * TODO: Autocomplete settings init
         */
        tagsTextView = (MultiAutoCompleteTextView)fragmentView.findViewById(R.id.autoCompleteTags);
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onExpenseAdded(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnExpenseAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Expense getNewExpense(){
        EditText valueET = (EditText)getView().findViewById(R.id.editText2);
        String value =  valueET.getText().toString();

        String tagsStr = tagsTextView.getText().toString();
        StringTokenizer strTok = new StringTokenizer(tagsStr);
        ArrayList <String> tags = new ArrayList<String>();
        while (strTok.hasMoreElements())
            tags.add(strTok.nextToken());

        addTagsToAutoComplete(tags);

        EditText dateET = (EditText)getView().findViewById(R.id.editText);
        Date date = null;
        try {
            date = Expense.dateFormat.parse(dateET.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // clear fields
        valueET.setText("");
        tagsTextView.setText("");
        dateET.setText("");

        Calendar c = Calendar.getInstance();
        dateExpenseCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
        dateExpenseCalendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
        dateExpenseCalendar.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));

        return new Expense(date, value, null, null);
    }

    public void addTagsToAutoComplete(ArrayList<String> tags)
    {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tags);
        tagsTextView.setAdapter(adapter);
        tagsTextView.setTokenizer(new SpaceTokenizer());
        tagsTextView.setThreshold(1);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExpenseAddedListener {
        // TODO: Update argument type and name
        public void onExpenseAdded(Uri uri);
    }

    /**
     * This is a space tokenizer to be used in the MultiAutoCompleteTextView.
     *
     * The code is the answer from a StackOverflow question:
     * http://stackoverflow.com/questions/3482981/how-to-replace-the-comma-with-a-space-when-i-use-the-multiautocompletetextview
     */
    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
}

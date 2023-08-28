package com.example.qrscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SHARED_PREFS = "sharedID";
    FirebaseFirestore firebaseFirestore;
    private CollectionReference userRef;
    private DocumentReference userDocRef;
    private String userid;

    private TransactionAdapter2 adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static boolean firstRun = true;

    private ListView transactionListView;

    //ListView lv;

    //ArrayAdapter<MyTransaction> adapter;
    //Transaction transactionData;
    //Transaction[] data;

    MyTransaction[] data;
    TextView transactionDataText;

    RecyclerView recyclerView;
    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef  = firebaseFirestore.collection("user");



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        initWidgets();
        //setTransactionAdapter();
        //lv = (ListView) view.findViewById(R.id.transactionListView);
        //adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,  data);
        //lv.setAdapter(adapter);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        userDocRef = userRef.document(userid);
        System.out.println("My userid (transaction): " + userid);


        //transactionDataText = view.findViewById(R.id.transaction_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        setTransactionAdapter();

        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists())
                {
                    MyTransaction transaction = documentSnapshot.toObject(MyTransaction.class);

                    int id = transaction.getId();
                    String date = transaction.getDate();
                    String time = transaction.getTime();
                    String duration = transaction.getDuration();
                    Double amt = transaction.getTotalAmt();


                }

            }
        });



        loadTransaction();
        setUpRecyclerView();




        //setUpRecyclerView();
        //fetchData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }

    private void initWidgets() {
        transactionListView = this.getActivity().findViewById(R.id.transactionListView);
    }

    private void setTransactionAdapter()
    {
        //TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity().getApplicationContext(), MyTransaction.transactionArrayList);
        //lv.setAdapter(transactionAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }
    @Override
    public void onResume() {

        super.onResume();
        adapter.startListening();

        //setTransactionAdapter();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public void fetchData()
    {
        userDocRef.addSnapshotListener(this.getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null)
                {
                    return;
                }


                if (documentSnapshot.exists())
                {
                    Transaction transaction = documentSnapshot.toObject(Transaction.class);



                }
            }
        });
    }

    public void loadTransaction()
    {
        userRef.document(userid).collection("transaction").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            MyTransaction transaction = documentSnapshot.toObject(MyTransaction.class);

                            int id = transaction.getId();
                            String date = transaction.getDate();
                            String time = transaction.getTime();
                            String duration = transaction.getDuration();
                            Double amt = transaction.getTotalAmt();

                            data += (id + "\n" + date + "\n" + time + "\n" + duration + "\n" + amt);

                        }

                        //transactionDataText.setText(data);


                    }
                });
    }

    private void setUpRecyclerView()
    {
        Query query = userRef.document(userid).collection("transaction").orderBy("id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MyTransaction> options = new FirestoreRecyclerOptions.Builder<MyTransaction>()
                .setQuery(query, MyTransaction.class)
                .build();

        adapter = new TransactionAdapter2(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



    }


}
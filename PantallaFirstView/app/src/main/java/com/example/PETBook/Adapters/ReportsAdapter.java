package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.ReportsFragment;
import com.example.PETBook.Models.ReportModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReportsAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<ReportModel> reportsList;

    public ReportsAdapter(Context context, ArrayList<ReportModel> reportsList){
        this.context = context;
        this.reportsList = reportsList;
    }

    @Override
    public int getCount() {
        if (reportsList != null)
            return reportsList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return reportsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.report_design,null);
        }


        TextView userRepored = (TextView) convertView.findViewById(R.id.userReported);
        TextView userReporting = (TextView) convertView.findViewById(R.id.userWhoReported);
        TextView hora = (TextView) convertView.findViewById(R.id.horaReport);
        TextView textReport = (TextView) convertView.findViewById(R.id.textReport);

        userReporting.setText(reportsList.get(position).getEmailUserReporting());
        userRepored.setText(reportsList.get(position).getEmailUserReported());
        textReport.setText(reportsList.get(position).getDescription());



       String fechaString =  reportsList.get(position).getCreationDate();

        Date dateNew = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            dateNew = format.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        hora.setText(prettyTime.format(dateNew));


        final String[] options = {"Report user", "Reject report"};
        final View finalConvertView = convertView;


        View.OnClickListener  listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("numero which:" + which);
                        if (which == 0) {
                            reportUser(position);
                        } else if (which == 1) {
                            discardReport(position);
                        }
                    }
                });
                builder.show();
            }



        };

        userRepored.setOnClickListener(listener);
        userReporting.setOnClickListener(listener);
        textReport.setOnClickListener(listener);



        return convertView;
    }

    private void discardReport(int position) {

        String idReport = reportsList.get(position).getId();

        Conexion con = new Conexion(ReportsAdapter.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/reports/" + idReport + "/voteReject","POST", null);
    }

    private void reportUser(int position) {

        String idReport = reportsList.get(position).getId();

        Conexion con = new Conexion(ReportsAdapter.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/reports/" + idReport + "/voteApprove","POST", null);
    }

    @Override
    public void OnprocessFinish(JSONObject output) {


    }
}

package com.teamname.uniroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamname.uniroll.database.entity.Subject;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    public interface OnSubjectActionListener {
        void onEdit(Subject subject);
        void onDelete(Subject subject);
    }

    private List<Subject> subjects;
    private final OnSubjectActionListener listener;

    public SubjectAdapter(List<Subject> subjects, OnSubjectActionListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    public void updateList(List<Subject> newSubjects) {
        this.subjects = newSubjects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.tvSubjectName.setText(subject.subjectName);
        holder.tvSubjectCode.setText(subject.subjectCode);
        holder.tvCreditHours.setText(subject.creditHours + " credit hours");

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(subject));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(subject));
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvSubjectCode, tvCreditHours;
        Button btnEdit, btnDelete;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectCode = itemView.findViewById(R.id.tvSubjectCode);
            tvCreditHours = itemView.findViewById(R.id.tvCreditHours);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

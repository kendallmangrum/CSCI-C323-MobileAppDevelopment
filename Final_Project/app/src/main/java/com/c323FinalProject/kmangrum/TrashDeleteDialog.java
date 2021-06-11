package com.c323FinalProject.kmangrum;

import androidx.fragment.app.DialogFragment;

import com.c323FinalProject.kmangrum.daos.TrashDAO;

public class TrashDeleteDialog extends DialogFragment {

    TrashDAO trashDAO;
    AppDatabase database;

    public interface CustomTrashDialogListener{
        void onDialogComplete();
    }

    public void sendResults() {
        CustomTrashDialogListener listener = (CustomTrashDialogListener) getTargetFragment();
        listener.onDialogComplete();
        dismiss();
    }


}

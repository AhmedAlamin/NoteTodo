package com.ahmedalamin.notetodo.ui.deleteallcompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment :DialogFragment(){


    private val viewModel : DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =

        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you relly want to delete completed tasks")
            .setNegativeButton("cancel", null)
            .setPositiveButton("yes"){ _,_ ->
                viewModel.onConfirmClick()
            }
            .create()


}
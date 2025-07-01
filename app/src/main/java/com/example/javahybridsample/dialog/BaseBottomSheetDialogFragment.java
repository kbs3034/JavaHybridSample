package com.example.javahybridsample.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.javahybridsample.R;
import com.example.javahybridsample.util.CommonUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    /**
     * 외부 클릭시 닫기 여부 설정, 기본값 : false
     */
    private Boolean shouldCancellableOnTouchOutside = false;

    /**
     * 드래그 여부, 기본값 : 드래그 불가능(false)
     */
    private Boolean  isDraggable = false;

    public Object binding;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bsd = (BottomSheetDialog) dialogInterface;
                View bottomSheet = bsd.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                bsd.setCanceledOnTouchOutside(shouldCancellableOnTouchOutside);
                bottomSheet.setBackgroundColor(getContext().getColor(R.color.transparent));

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setDraggable(isDraggable);
            }
        });
    }

    @Override
    public int getTheme() {
        return R.style.CustomThemeBottomSheet;
    }

    private int headerHeight = CommonUtil.getPx(56); //헤더 높이

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

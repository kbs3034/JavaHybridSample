package com.example.javahybridsample.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.javahybridsample.databinding.BottomSheetCommonBinding;
import com.example.javahybridsample.util.StringUtil;

public class CommonBottomSheetDialog extends BaseBottomSheetDialogFragment {

    private BottomSheetOption bottomSheetOption;

    private BottomSheetCommonBinding binding;
    private BottomSheetButtonClickListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCommonBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        if (bottomSheetOption.title == null) {
            throw new IllegalArgumentException("title");
        }

        if (bottomSheetOption.desc == null) {
            throw new IllegalArgumentException("desc");
        }

        binding.tvTitle.setText(bottomSheetOption.title);
        binding.tvDesc.setText(bottomSheetOption.desc);

        binding.leftTv.setText(bottomSheetOption.leftButtonTitle);
        binding.rightTv.setText(bottomSheetOption.rightButtonTitle);

        if(bottomSheetOption.isOneButton) {
            binding.cardLeftBtn.setVisibility(View.GONE);
        }

        binding.cardLeftBtn.setOnClickListener(view -> {
            mListener.onLeftButtonClicked(view);
            dismiss();
        });

        binding.cardRightBtn.setOnClickListener(view -> {
            mListener.onRightButtonClicked(view);
            dismiss();
        });

        return root;
    }

    public void setClickListener(BottomSheetButtonClickListener listener){
        this.mListener = listener;
    }
    public void setBottomSheetOption(BottomSheetOption bottomSheetOption){
        this.bottomSheetOption = bottomSheetOption;
    }
    public interface BottomSheetButtonClickListener {
        void onLeftButtonClicked(View view);
        void onRightButtonClicked(View view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class BottomSheetOption {
        String title;
        String desc;

        boolean isOneButton = false;
        String leftButtonTitle = "취소";
        String rightButtonTitle = "확인";

        public BottomSheetOption(String title, String desc){
            this.title = title;
            this.desc = desc;
        }
        public BottomSheetOption(String title, String desc, boolean isOneButton, String leftButtonTitle, String rightButtonTitle){
            this.title = title;
            this.desc = desc;
            this.isOneButton = isOneButton;
            if(StringUtil.isNotEmpty(leftButtonTitle)) this.leftButtonTitle = leftButtonTitle;
            if(StringUtil.isNotEmpty(rightButtonTitle)) this.rightButtonTitle = rightButtonTitle;
        }
    }
}

package study.smart.transfer_management.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.TransferDetailentity;

/**
 * @author yqy
 * @date on 2018/6/29
 * @describe 合同及基本信息
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ContractInformationFragment extends UIFragment {

    private TransferDetailentity transferDetailentity;
    private String orderState;


    public static ContractInformationFragment getInstance(Bundle bundle) {
        ContractInformationFragment fragment = new ContractInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_contract_information, null);
    }

    @Override
    protected void initView(View rootView) {
        transferDetailentity = (TransferDetailentity) getArguments().getSerializable("transferDetailentity");
        orderState = getArguments().getString("orderState");
        TextView tvContractNumber = rootView.findViewById(R.id.tv_contract_number);
        TextView tv_receiptId = rootView.findViewById(R.id.tv_receiptId);
        TextView tvName = rootView.findViewById(R.id.tv_name);
        TextView tvSex = rootView.findViewById(R.id.tv_sex);
        TextView tvPhone = rootView.findViewById(R.id.tv_phone);
        TextView tvEmail = rootView.findViewById(R.id.tv_email);
        TextView tvParentPhone = rootView.findViewById(R.id.tv_parent_phone);
        TextView tvCity = rootView.findViewById(R.id.tv_city);
        TextView tvSchool = rootView.findViewById(R.id.tv_school);
        TextView tvGrade = rootView.findViewById(R.id.tv_grade);
        TextView tvTime = rootView.findViewById(R.id.tv_time);
        TextView tvWorkingYear = rootView.findViewById(R.id.tv_working_year);
        TextView tvSignedTime = rootView.findViewById(R.id.tv_signed_time);
        TextView tvProject = rootView.findViewById(R.id.tv_project);
        TextView tvMoney = rootView.findViewById(R.id.tv_money);
        TextView tvType = rootView.findViewById(R.id.tv_type);
        TextView tvCountry = rootView.findViewById(R.id.tv_country);
        TextView tvTargetDegree = rootView.findViewById(R.id.tv_target_degree);
        TextView tvTargetMajor = rootView.findViewById(R.id.tv_target_major);
        TextView tvContractor = rootView.findViewById(R.id.tv_contractor);
        TextView tvContractorTeacher = rootView.findViewById(R.id.tv_contractor_teacher);
        TextView tvCounselor = rootView.findViewById(R.id.tv_counselor);
        TextView tvTemperamentTrait = rootView.findViewById(R.id.tv_temperament_trait);
        TextView tvUserSpecialDemand = rootView.findViewById(R.id.tv_user_special_demand);
        TextView tvSupplementContent = rootView.findViewById(R.id.tv_supplement_content);
        TextView tvOther = rootView.findViewById(R.id.tv_other);
        ImageView iv_over = rootView.findViewById(R.id.iv_over);

        if (transferDetailentity != null) {
            //合同信息
            if (transferDetailentity.getContractBase() != null) {
                tvContractNumber.setText(transferDetailentity.getContractBase().getContractId());
                tv_receiptId.setText(transferDetailentity.getContractBase().getReceiptId());
            }
            if ("已结案".equals(orderState)) {
                iv_over.setVisibility(View.VISIBLE);
            } else {
                iv_over.setVisibility(View.GONE);
            }
            //用户基本信息
            if (transferDetailentity.getUser() != null) {
                tvName.setText(transferDetailentity.getUser().getName());
                tvSex.setText(transferDetailentity.getUser().getSex());
                tvPhone.setText(transferDetailentity.getUser().getPhone());
                tvEmail.setText(transferDetailentity.getUser().getEmail());
                tvParentPhone.setText(transferDetailentity.getUser().getParentPhone());
                tvCity.setText(transferDetailentity.getUser().getCity());
                tvSchool.setText(transferDetailentity.getUser().getSchool());
                tvGrade.setText(transferDetailentity.getUser().getGrade());
                tvTime.setText(transferDetailentity.getUser().getGraduationTime());
                tvWorkingYear.setText(transferDetailentity.getUser().getWorkYears());
            }

            //签约信息
            if (transferDetailentity.getContractData() != null) {
                tvSignedTime.setText(transferDetailentity.getContractData().getSignedTime());
                tvProject.setText(transferDetailentity.getContractData().getServiceProductNames());
                tvMoney.setText(transferDetailentity.getContractData().getServiceFee());
                tvType.setText(transferDetailentity.getContractData().getPaymentMethod());
                tvCountry.setText(transferDetailentity.getContractData().getTargetCountry());
                tvTargetDegree.setText(transferDetailentity.getContractData().getTargetDegree());
                tvTargetMajor.setText(transferDetailentity.getContractData().getTargetMajor());
                tvContractor.setText(transferDetailentity.getContractData().getContractor());
                tvContractorTeacher.setText(transferDetailentity.getContractData().getContractorTeacher());
                tvCounselor.setText(transferDetailentity.getContractData().getCounselor());
                tvTemperamentTrait.setText(transferDetailentity.getContractData().getTemperamentTrait());
                tvUserSpecialDemand.setText(transferDetailentity.getContractData().getUserSpecialDemand());
                tvSupplementContent.setText(transferDetailentity.getContractData().getSupplementContent());
                tvOther.setText(transferDetailentity.getContractData().getOtherContent());
            }
        }

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}

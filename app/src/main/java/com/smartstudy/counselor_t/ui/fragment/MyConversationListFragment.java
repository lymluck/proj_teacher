package com.smartstudy.counselor_t.ui.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.smartstudy.counselor_t.ui.adapter.MeConversationAdapter;

import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


public class MyConversationListFragment extends ConversationListFragment {

    @Override

    public ConversationListAdapter onResolveAdapter(Context context) {

        return new MeConversationAdapter(getActivity());
    }


}
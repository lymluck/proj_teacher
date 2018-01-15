package com.smartstudy.counselor_t.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.smartstudy.counselor_t.R;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;

/**
 * Created by yqy on 2017/12/27.
 */

public class MeConversationAdapter extends ConversationListAdapter {
    private Context mContext;
    LayoutInflater mInflater;
    private MeConversationAdapter.OnPortraitItemClick mOnPortraitItemClick;

    public MeConversationAdapter(Context context) {
        super(context);
        mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }


    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View result = this.mInflater.inflate(R.layout.fragment_item_conversation, (ViewGroup) null);
        MeConversationAdapter.ViewHolder holder = new MeConversationAdapter.ViewHolder();
        holder.layout = this.findViewById(result, R.id.rc_item_conversation);
        holder.leftImageLayout = this.findViewById(result, R.id.rc_item1);
        holder.rightImageLayout = this.findViewById(result, R.id.rc_item2);
        holder.leftUnReadView = this.findViewById(result, R.id.rc_unread_view_left);
        holder.rightUnReadView = this.findViewById(result, R.id.rc_unread_view_right);
        holder.leftImageView = (AsyncImageView) this.findViewById(result, R.id.rc_left);
        holder.rightImageView = (AsyncImageView) this.findViewById(result, R.id.rc_right);
        holder.contentView = (ProviderContainerView) this.findViewById(result, R.id.rc_content);
        holder.unReadMsgCount = (TextView) this.findViewById(result, R.id.rc_unread_message);
        holder.unReadMsgCountRight = (TextView) this.findViewById(result, R.id.rc_unread_message_right);
        holder.unReadMsgCountIcon = (ImageView) this.findViewById(result, R.id.rc_unread_message_icon);
        holder.unReadMsgCountRightIcon = (ImageView) this.findViewById(result, R.id.rc_unread_message_icon_right);
        result.setTag(holder);
        return result;
    }


    @Override
    protected void bindView(View v, int position, final UIConversation data) {
        MeConversationAdapter.ViewHolder holder = (MeConversationAdapter.ViewHolder) v.getTag();
        if (data != null) {
            IContainerItemProvider provider = RongContext.getInstance().getConversationTemplate(data.getConversationType().getName());
            if (provider == null) {
                RLog.e("ConversationListAdapter", "provider is null");
            } else {
                View view = holder.contentView.inflate(provider);
                provider.bindView(view, position, data);
                if (data.isTop()) {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(io.rong.imkit.R.drawable.rc_item_top_list_selector));
                } else {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(io.rong.imkit.R.drawable.rc_item_list_selector));
                }

                ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(data.getConversationType().getName());

                int defaultId;

                if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                    defaultId = io.rong.imkit.R.drawable.rc_default_group_portrait;
                } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                    defaultId = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                } else {
                    defaultId = io.rong.imkit.R.drawable.rc_default_portrait;
                }

                if (tag.portraitPosition() == 1) {
                    holder.leftImageLayout.setVisibility(View.VISIBLE);
                    holder.leftImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (MeConversationAdapter.this.mOnPortraitItemClick != null) {
                                MeConversationAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.leftImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (MeConversationAdapter.this.mOnPortraitItemClick != null) {
                                MeConversationAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    if (data.getConversationGatherState()) {
                        holder.leftImageView.setAvatar((String) null, defaultId);
                    } else if (data.getIconUrl() != null) {
                        holder.leftImageView.setAvatar(data.getIconUrl().toString(), defaultId);
                    } else {
                        holder.leftImageView.setAvatar((String) null, defaultId);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountIcon.setVisibility(View.VISIBLE);
                        this.setUnReadViewLayoutParams(holder.leftUnReadView, data.getUnReadType());
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCount.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCount.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_list_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.rightImageLayout.setVisibility(View.GONE);
                } else if (tag.portraitPosition() == 2) {
                    holder.rightImageLayout.setVisibility(View.VISIBLE);
                    holder.rightImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (MeConversationAdapter.this.mOnPortraitItemClick != null) {
                                MeConversationAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.rightImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (MeConversationAdapter.this.mOnPortraitItemClick != null) {
                                MeConversationAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    if (data.getConversationGatherState()) {
                        holder.rightImageView.setAvatar((String) null, defaultId);
                    } else if (data.getIconUrl() != null) {
                        holder.rightImageView.setAvatar(data.getIconUrl().toString(), defaultId);
                    } else {
                        holder.rightImageView.setAvatar((String) null, defaultId);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountRightIcon.setVisibility(View.VISIBLE);
                        this.setUnReadViewLayoutParams(holder.rightUnReadView, data.getUnReadType());
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCountRight.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCountRight.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_without_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.leftImageLayout.setVisibility(View.GONE);
                } else {
                    if (tag.portraitPosition() != 3) {
                        throw new IllegalArgumentException("the portrait position is wrong!");
                    }

                    holder.rightImageLayout.setVisibility(View.GONE);
                    holder.leftImageLayout.setVisibility(View.GONE);
                }

            }
        }
    }


    @Override
    public void setOnPortraitItemClick(ConversationListAdapter.OnPortraitItemClick onPortraitItemClick) {
        this.mOnPortraitItemClick = onPortraitItemClick;
    }


    protected class ViewHolder {
        public View layout;
        public View leftImageLayout;
        public View rightImageLayout;
        public View leftUnReadView;
        public View rightUnReadView;
        public AsyncImageView leftImageView;
        public TextView unReadMsgCount;
        public ImageView unReadMsgCountIcon;
        public AsyncImageView rightImageView;
        public TextView unReadMsgCountRight;
        public ImageView unReadMsgCountRightIcon;
        public ProviderContainerView contentView;

        protected ViewHolder() {
        }
    }
}

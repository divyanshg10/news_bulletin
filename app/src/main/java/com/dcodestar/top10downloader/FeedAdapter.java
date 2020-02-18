package com.dcodestar.top10downloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Feed> feeds;

    public FeedAdapter(Context context, int resource, List<Feed> feeds) {
        super(context, resource);
        this.layoutResource=resource;
        this.layoutInflater=LayoutInflater.from(context);
        this.feeds = feeds;
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(layoutResource,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        Feed feed=feeds.get(position);
        viewHolder.title.setText(feed.getTitle());
        viewHolder.description.setText(feed.getDescription());
        return convertView;
    }
    private class ViewHolder{
        private final TextView title;
        private final TextView description;
        ViewHolder(View v){
            title=v.findViewById(R.id.Title);
            description=v.findViewById(R.id.Description);
        }
    }
}

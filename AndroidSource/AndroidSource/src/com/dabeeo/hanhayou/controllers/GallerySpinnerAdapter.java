package com.dabeeo.hanhayou.controllers;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dabeeo.hanhayou.R;

@SuppressLint("InflateParams")
public class GallerySpinnerAdapter extends ArrayAdapter<String>
{
  
  private ArrayList<String> items;
  private Context context;
  
  
  public GallerySpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> items)
  {
    super(context, textViewResourceId, items);
    this.context = context;
    this.items = items;
  }
  
  
  @SuppressLint("ViewHolder")
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    View v = convertView;
    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    v = vi.inflate(R.layout.view_gallery_spinner, null);
    
    String item = items.get(position);
    TextView tt = (TextView) v.findViewById(R.id.title);
    tt.setText(item);
    return v;
  }
  
  
  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent)
  {
    View v = convertView;
    LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    v = vi.inflate(R.layout.view_gallery_spinner, null);
    
    String item = items.get(position);
    TextView tt = (TextView) v.findViewById(R.id.title);
    tt.setText(item);
    return v;
  }
}

package br.ufms.vagner.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufms.vagner.agenda.R;
import br.ufms.vagner.agenda.model.Contato;
import br.ufms.vagner.agenda.util.BitmapUtils;
import br.ufms.vagner.agenda.util.ImageConverter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ListViewAdapter extends ArrayAdapter<Contato> {
    Context context;
    LayoutInflater inflater;
    List<Contato> contatoList;
    private SparseBooleanArray mSelectedItemsIds;
    private final Context mContext;
    private Filter filter;

    public ListViewAdapter(Context context, int resourceId, List<Contato> contatoList) {
        super(context, resourceId, contatoList);
        this.mContext = context;

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.contatoList = contatoList;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        @Bind(R.id.nome)
        TextView nome;
        @Bind(R.id.telefone)
        TextView telefone;
        @Bind(R.id.email)
        TextView email;
        @Bind(R.id.imagem)
        ImageView imagem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nome.setText(contatoList.get(position).getNome());
        holder.telefone.setText(contatoList.get(position).getTelefone());
        holder.email.setText(contatoList.get(position).getEmail());
        String S = contatoList.get(position).getImagem();

        Bitmap bitmap = BitmapUtils.getBitmapFromImgString(contatoList.get(position).getImagem(), mContext);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
        holder.imagem.setImageBitmap(circularBitmap);
        return view;
    }

    public void setListItem(List<Contato> listItem) {
        this.contatoList = listItem;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<Contato>(contatoList);
        return filter;
    }

    @Override
    public void remove(Contato object) {
        contatoList.remove(object);
        notifyDataSetChanged();
    }

    public List<Contato> getContatoList() {
        return contatoList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Contato) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
}
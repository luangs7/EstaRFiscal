package br.com.tads.estarfiscal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


import br.com.tads.estarfiscal.R;
import br.com.tads.estarfiscal.model.Estar;


/**
 * Created by DevMaker on 3/16/16.
 */
public class EnderecoAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Estar> veiculos;


    public EnderecoAdapter(Context context, List<Estar> veiculos) {
        this.mContext = context;
        this.veiculos = veiculos;

    }

    @Override
    public int getCount() {
        return veiculos.size();
    }

    @Override
    public Estar getItem(int position) {
        return veiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        final Estar estar = veiculos.get(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_enderecos, parent, false);

            holder = new ViewHolder();
            holder.placa = (TextView) convertView.findViewById(R.id.txtPlaca);
            holder.endereco = (TextView) convertView.findViewById(R.id.txtEndereco);
            holder.tempo = (TextView) convertView.findViewById(R.id.txtTempo);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.placa.setText(estar.getPlaca());
        holder.endereco.setText(estar.getAddress());
        Integer vencido = Integer.valueOf(estar.getVencido());

        if(vencido == 0 ){
            Integer restante = Integer.valueOf(estar.getHoras()) - Integer.valueOf(estar.getDiff());
            holder.tempo.setText("Restam: " + restante + " minutos");
            holder.layout.setBackgroundColor(Color.parseColor("#218328"));
        }else{
            holder.tempo.setText("VENCIDO");
            holder.layout.setBackgroundColor(Color.parseColor("#ff1919"));
        }

        return convertView;
    }

    class ViewHolder {
        public TextView placa;
        public TextView endereco;
        public TextView tempo;
        public RelativeLayout layout;
    }

}

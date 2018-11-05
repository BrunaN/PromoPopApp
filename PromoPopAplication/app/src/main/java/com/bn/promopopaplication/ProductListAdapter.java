package com.bn.promopopaplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{

    private List<Produto> dataModelList;
    private Context mContext;
    private int itemLayout;

    // View holder class whose objects represent each list item

    private static ItemClickListener itemClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public ImageView productImage, storeImage;
        public TextView productName, storeName, productTime, productPriceBefore, productPrice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView);
            storeImage = itemView.findViewById(R.id.storeImage);
            productName = itemView.findViewById(R.id.productName);
            storeName = itemView.findViewById(R.id.storeName);
            productTime = itemView.findViewById(R.id.productTime);
            productPriceBefore = itemView.findViewById(R.id.productPriceBefore);
            productPrice = itemView.findViewById(R.id.productPrice);

            itemView.setOnClickListener(this);
        }

        public void bindData(Produto produto, Context context) {
            //Setar a imagem do produto aqui
            //productImage.setImageDrawable(ContextCompat.getDrawable(context, IMAGEM DO PRODUTO));

            //Setar a imagem da loja aqui
            //productImage.setImageDrawable(ContextCompat.getDrawable(context, IMAGEM DA LOJA));


            productName.setText(produto.getNomeProduto());
            storeName.setText(produto.getNomeLoja());
            productTime.setText(produto.getDiasRestantes()+" dias restantes");
            productPriceBefore.setText("R$ "+produto.getPrecoAnterior());
            productPrice.setText("R$ "+produto.getPreco());
        }

        @Override
        public void onClick(View v) {
            Log.d("TESTE", "TESTE");
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ProductListAdapter(List<Produto> modelList, Context context, int itemLayout) {
        dataModelList = modelList;
        this.itemLayout = itemLayout;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        // Return a new view holder

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position

        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items

        return dataModelList.size();
    }
}
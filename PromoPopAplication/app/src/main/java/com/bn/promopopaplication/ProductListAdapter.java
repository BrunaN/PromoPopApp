package com.bn.promopopaplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bn.promopopaplication.Activity.MainActivity;
import com.bn.promopopaplication.Activity.MainStoreActivity;
import com.bn.promopopaplication.Activity.ProductActivity;
import com.bn.promopopaplication.DAO.ConfigurationFirebase;
import com.bn.promopopaplication.Entity.Product;
import com.bn.promopopaplication.Entity.Store;
import com.bn.promopopaplication.Entity.Users;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.bn.promopopaplication.R.drawable.ic_favorite;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{

    private List<Product> dataModelList;
    private Context mContext;
    private int itemLayout;
    // View holder class whose objects represent each list item

    private static ItemClickListener itemClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView productImage, storeImage, noImage;
        public TextView productName, storeName, productTime, productPriceBefore, productPrice;
        public ImageButton addWishList, wishList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addWishList = itemView.findViewById(R.id.addWishList);
            wishList = itemView.findViewById(R.id.wishList);
            productImage = itemView.findViewById(R.id.imageProduct);
            storeImage = itemView.findViewById(R.id.storeImage);
            noImage = itemView.findViewById(R.id.noImage);
            productName = itemView.findViewById(R.id.productName);
            storeName = itemView.findViewById(R.id.storeName);
            productTime = itemView.findViewById(R.id.productTime);
            productPriceBefore = itemView.findViewById(R.id.productPriceBefore);
            productPrice = itemView.findViewById(R.id.productPrice);


            itemView.setOnClickListener(this);
        }

        public void bindData(final Product produto, final Context context) {

            FirebaseAuth firebaseAuth = ConfigurationFirebase.getFirebaseAuthtication();
            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            final List<String> wishedList = new ArrayList<String>();

            if(firebaseUser != null){
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                String id = firebaseUser.getUid();

                DatabaseReference ref = database.getReference("user/"+ id);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue() != null){

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("user/"+ firebaseUser.getUid()).child("wishedProducts");

                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                        wishedList.add(snapshot.getValue().toString());
                                        if(snapshot.getValue().toString().equals(produto.getId())){
                                            wishList.setVisibility(View.VISIBLE);
                                            addWishList.setVisibility(View.GONE);
                                        }
                                    }

                                    addWishList.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            for(int i=0; i < wishedList.size(); i++){
                                                if(produto.getId() == wishedList.get(i)){
                                                    Toast.makeText(context, "Esse Produto já está adicionado na sua Lista de desejo!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            wishList.setVisibility(View.VISIBLE);
                                            addWishList.setVisibility(View.GONE);

                                            Users user = new Users();
                                            user.setId(firebaseUser.getUid());

                                            String idProduct = produto.getId();
                                            user.addWished(idProduct);
                                            Toast.makeText(context, "Esse Produto foi adicionado na sua Lista de desejo!", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("FIREBASE DATABASE", "loadPost:onCancelled", databaseError.toException());
                                }
                            });

                        } else {

                            addWishList.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(context, "Este produto apenas pode ser adicionado na Lista de desejo de perfis sem loja", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

            } else {

                addWishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Para adicionar o produto na sua Lista de desejo, você precisa fazer login", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            final Store store = new Store();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ produto.getImage());

            if(produto.getImage() != null) {
                Glide.with(context)
                        .load(storageReference)
                        .into(productImage);
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("stores/" + produto.getIdLoja());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //String id = dataSnapshot.child("id").getValue(String.class);
                    String id = (String) dataSnapshot.child("id").getValue();
                    String name = (String) dataSnapshot.child("storeName").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();
                    String endereco = (String) dataSnapshot.child("endereco").getValue();
                    String cidade = (String) dataSnapshot.child("cidade").getValue();

                    store.setId(id);
                    store.setStoreName(name);
                    store.setEmail(email);
                    store.setEndereco(endereco);
                    store.setCidade(cidade);

                    storeName.setText(store.getStoreName());

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+ image);

                    if(image != null) {

                        store.setImage(image);

                        Glide.with(context)
                                .load(storageReference)
                                .into(storeImage);

                        storeImage.setVisibility(View.VISIBLE);
                        noImage.setVisibility(View.GONE);

                    }else{
                        storeImage.setVisibility(View.GONE);
                        noImage.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }

            });


            Log.d("teste", "produto: "+ produto.getId());

            productName.setText(produto.getNomeProduto());
            productTime.setText(produto.getDiasRestantes()+" dias restantes");
            productPriceBefore.setText("R$ "+produto.getPrecoAnterior());
            productPrice.setText("R$ "+produto.getPreco());

            productPriceBefore.setPaintFlags(productPriceBefore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ProductListAdapter(List<Product> modelList, Context context, int itemLayout) {
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
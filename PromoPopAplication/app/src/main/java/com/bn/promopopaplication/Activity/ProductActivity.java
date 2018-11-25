package com.bn.promopopaplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bn.promopopaplication.Produto;
import com.bn.promopopaplication.R;

public class ProductActivity extends AppCompatActivity {

    private TextView productName, storeName, productTime, productPriceBefore, productPrice;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        produto = (Produto) intent.getSerializableExtra("produto");

        productName = findViewById(R.id.productName);
        storeName = findViewById(R.id.storeName);
        productTime = findViewById(R.id.productTime);
        productPriceBefore = findViewById(R.id.productPriceBefore);
        productPrice = findViewById(R.id.productPrice);


        productName.setText(produto.getNomeProduto());
        storeName.setText(produto.getNomeLoja());
        productTime.setText(produto.getDiasRestantes()+" dias restantes");
        productPriceBefore.setText("R$ "+produto.getPrecoAnterior());
        productPrice.setText("R$ "+produto.getPreco());
    }

}

package com.stiwy.dstorapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stiwy.dstorapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    daoContacto dao;
    Adaptador adapter;
    ArrayList<Contacto> Lista;
    Contacto c;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dao = new daoContacto(this.getContext());
        Lista = dao.verTodos();
        adapter = new Adaptador(this.getActivity(), Lista, dao);
        ListView list = (ListView) root.findViewById(R.id.lista);
        Button agregar = (Button) root.findViewById(R.id.agregar);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(HomeFragment.this.getContext());
                dialog.setTitle("Nuevo registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo);
                dialog.show();
                final EditText nombre = (EditText) dialog.findViewById(R.id.nombre);
                final EditText apellido = (EditText) dialog.findViewById(R.id.apellido);
                final EditText email = (EditText) dialog.findViewById(R.id.email);
                final EditText edad = (EditText) dialog.findViewById(R.id.edad);
                Button guardar = (Button) dialog.findViewById(R.id.d_agregar);
                guardar.setText("Agregar");
                Button cancelar = (Button) dialog.findViewById(R.id.d_cancelar);
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            c = new Contacto(nombre.getText().toString(),
                                    apellido.getText().toString(),
                                    email.getText().toString(),
                                    Integer.parseInt(edad.getText().toString()));
                            dao.insertar(c);
                            Lista=dao.verTodos();
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }


                });
            }
        });
        return root;
    }
}
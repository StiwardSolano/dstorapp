package com.stiwy.dstorapp.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stiwy.dstorapp.R;

import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;

public class Adaptador extends BaseAdapter{
    ArrayList<Contacto> Lista;
    daoContacto dao;
    Contacto c;
    Activity a;
    int id=0;

    public Adaptador(Activity a,ArrayList<Contacto> Lista, daoContacto dao) {
        this.Lista = Lista;
        this.a = a;
        this.dao = dao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return Lista.size();
    }

    @Override
    public Contacto getItem(int i) {
        c=Lista.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        c=Lista.get(i);
        return c.getId();
    }

    @Override
    public View getView(int posicion, final View view, ViewGroup viewGroup) {
        View v=view;
        if(v==null){
            LayoutInflater li=(LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=li.inflate(R.layout.item,null);
        }
        c=Lista.get(posicion);
        TextView nombre=(TextView)v.findViewById(R.id.t_nombre);
        TextView apellido=(TextView)v.findViewById(R.id.t_apellido);
        TextView email=(TextView)v.findViewById(R.id.t_email);
        TextView edad=(TextView)v.findViewById(R.id.t_edad);
        Button editar=(Button)v.findViewById(R.id.editar);
        Button eliminar=(Button)v.findViewById(R.id.eliminar);
        nombre.setText(c.getNombre());
        apellido.setText(c.getApellido());
        email.setText(c.getEmail());
        edad.setText(""+c.getEdad());
        editar.setTag(posicion);
        eliminar.setTag(posicion);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=Integer.parseInt(view.getTag().toString());
                final Dialog dialog = new Dialog(a);
                dialog.setTitle("Editar registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo);
                dialog.show();
                final EditText nombre = (EditText) dialog.findViewById(R.id.nombre);
                final EditText apellido = (EditText) dialog.findViewById(R.id.apellido);
                final EditText email = (EditText) dialog.findViewById(R.id.email);
                final EditText edad = (EditText) dialog.findViewById(R.id.edad);
                Button guardar = (Button) dialog.findViewById(R.id.d_agregar);
                Button cancelar = (Button) dialog.findViewById(R.id.d_cancelar);
                guardar.setText("Guardar");
                c=Lista.get(pos);
                setId(c.getId());
                nombre.setText(c.getNombre());
                apellido.setText(c.getApellido());
                email.setText(c.getEmail());
                edad.setText(""+c.getEdad());
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            c = new Contacto(getId(),nombre.getText().toString(),
                                    apellido.getText().toString(),
                                    email.getText().toString(),
                                    Integer.parseInt(edad.getText().toString()));
                            dao.editar(c);
                            Lista=dao.verTodos();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(a, "ERROR", Toast.LENGTH_SHORT).show();
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
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=Integer.parseInt(view.getTag().toString());
                c=Lista.get(pos);
                setId(c.getId());
                AlertDialog.Builder del=new AlertDialog.Builder(a);
                del.setMessage("Estas seguro de eliminar registro?");
                del.setCancelable(false);
                del.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.eliminar(getId());
                        Lista=dao.verTodos();
                        notifyDataSetChanged();
                    }
                });
                del.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                del.show();
            }
        });
        return v;
    }
}

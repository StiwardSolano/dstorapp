package com.stiwy.dstorapp.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class daoContacto {
    SQLiteDatabase cx;
    ArrayList<Contacto> Lista=new ArrayList<Contacto>();
    Contacto c;
    Context ct;
    String nombreBD="BDContacto";
    String tabla="create table if not exists contacto (id integer primary key  autoincrement, nombre text, apellido text, email text, edad integer)";

    public daoContacto(Context c)
        {
        this.ct = c;
        cx = c.openOrCreateDatabase(nombreBD, c.MODE_PRIVATE, null);
        cx.execSQL(tabla);
        }
        public boolean insertar(Contacto c) {
            ContentValues contenedor = new ContentValues();
            contenedor.put("nombre",c.getNombre());
            contenedor.put("apellido",c.getApellido());
            contenedor.put("email",c.getEmail());
            contenedor.put("edad",c.getEdad());
            return (cx.insert("contacto",null,contenedor))>0;
        }

        public boolean eliminar(int id) {


        return (cx.delete("contacto","id="+id,null))>0;
        }

        public boolean editar(Contacto c) {
            ContentValues contenedor = new ContentValues();
            contenedor.put("nombre",c.getNombre());
            contenedor.put("apellido",c.getApellido());
            contenedor.put("email",c.getEmail());
            contenedor.put("edad",c.getEdad());
            return (cx.update("contacto",contenedor,"id=="+c.getId(),null))>0;


        }

        public ArrayList<Contacto> verTodos() {
            Lista.clear();
            Cursor curso=cx.rawQuery("select*from contacto",null);
            if(curso!=null &&curso.getCount()>0) {
                curso.moveToFirst();
                do {
                    Lista.add(new Contacto(curso.getInt(0),
                            curso.getString(1),
                            curso.getString(2),
                            curso.getString(3),
                            curso.getInt(4)));
                } while (curso.moveToNext());
            }
            return Lista;
        }
        public Contacto verUno(int posicion) {
            Cursor cursor=cx.rawQuery("select*from contacto",null);
            cursor.moveToPosition(posicion);
            c=new Contacto(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4));


            return c;
        }


}

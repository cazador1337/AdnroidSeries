package com.joao.api.banco;

import android.os.Parcel;
import android.os.Parcelable;

import com.joao.api.banco.annotations.Attribute;
import com.joao.api.banco.annotations.CharField;
import com.joao.api.banco.annotations.PrimaryKey;
import com.joao.api.banco.annotations.Table;
import com.joao.api.banco.core.AbstractModel;

/**
 * Created by joao on 11/06/2016.
 */
public class Sample extends AbstractModel<Sample> implements Parcelable {

    public final static Objects<Sample> objects = (AbstractModel.Objects<Sample>) AbstractModel.objects;

    @Attribute(column = "nome", type = String.class)
    @CharField(max_length = 50)
    private String nome;

    @Attribute(column = "id", type = int.class)
    @PrimaryKey(auto_generated = true)
    private int id;

    @Table(name = "pessoa")
    public Sample() {
        super(Sample.class);
    }

    protected Sample(Parcel in) {
        nome = in.readString();
        id = in.readInt();
    }

    public static final Creator<Sample> CREATOR = new Creator<Sample>() {
        @Override
        public Sample createFromParcel(Parcel in) {
            return new Sample(in);
        }

        @Override
        public Sample[] newArray(int size) {
            return new Sample[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeInt(id);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package leoneo7.bealawyer.base

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ryouken on 2016/11/07.
 */

class Category : Parcelable {
    var id: Int = 0
    var name: String
    var count: Int = 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(id)
        out.writeString(name)
        out.writeInt(count)
    }

    private constructor(`in`: Parcel) {
        id = `in`.readInt()
        name = `in`.readString()
        count = `in`.readInt()
    }

    constructor(id: Int, name: String, count: Int) {
        this.id = id
        this.name = name
        this.count = count
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(`in`: Parcel): Category {
                return Category(`in`)
            }

            override fun newArray(size: Int): Array<Category?> {
                return arrayOfNulls(size)
            }
        }
    }
}

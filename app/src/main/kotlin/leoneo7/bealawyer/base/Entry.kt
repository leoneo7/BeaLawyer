package leoneo7.bealawyer.base

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ryouken on 2016/11/03.
 */

data class Entry(var entryId: Int, var title: String, var image: String?, var numbering: String?,
                 var repeat: Int, var date: Long, var categoryId: Int, var categoryName: String) : Parcelable {

    constructor(source: Parcel) : this(source.readInt(), source.readString(), source.readString(), source.readString(), source.readInt(), source.readLong(), source.readInt(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(entryId)
        dest?.writeString(title)
        dest?.writeString(image)
        dest?.writeString(numbering)
        dest?.writeInt(repeat)
        dest?.writeLong(date)
        dest?.writeInt(categoryId)
        dest?.writeString(categoryName)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Entry> = object : Parcelable.Creator<Entry> {
            override fun createFromParcel(source: Parcel): Entry = Entry(source)
            override fun newArray(size: Int): Array<Entry?> = arrayOfNulls(size)
        }
    }
}

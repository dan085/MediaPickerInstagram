package me.ningsk.mediascanlibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

import me.ningsk.mediascanlibrary.config.MimeType;


public class LocalMedia implements Parcelable {

    private int id = -1;
    private String title;  // 文件名称
    private String path;  // 文件路径
    private long size;  // 文件大小
    private long dateAdded;  // 文件添加时间
    private long dateModified;  // 文件修改时间
    @MimeType.MediaMimeType
    private int mediaMimeType;
    private String mimeType;  // mime类型 image/jpeg
    private boolean isSelected;
    private int duration;
    private int albumId;
    private Long dateTaken;  // 文件添加时间
    private int width;  // 尺寸信息
    private int height;  // 尺寸信息

    private boolean isCrop;  // 是否裁剪过
    private String cropPath;  // 裁剪过后图像保存的path
    private String compressPath;  // 压缩过后图像保存的path
    private boolean isCompressed;  // 图像是否成功压缩
    public int position;
    private int num;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public int getMediaMimeType() {
        return mediaMimeType;
    }

    public void setMediaMimeType(int mediaMimeType) {
        this.mediaMimeType = mediaMimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public Long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public String getCropPath() {
        return cropPath;
    }

    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setCompressed(boolean compressed) {
        isCompressed = compressed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }



    /*@Override
    public String toString() {
        return "id --> " + id + "\n" +
                "title --> " + title + "\n" +
                "path --> " + path + "\n" +
                "size --> " + Formatter.formatFileSize(UIUtils.getContext(), size) + "\n" +
                "添加时间 --> " + TimeUtils.millis2String(dateAdded) + "\n" +
                "修改时间 --> " + TimeUtils.millis2String(dateModified) + "\n" +
                "mimeType --> " + mimeType;
    }*/

    @Override
    public boolean equals(Object o) {
        /*if (this == o)
            return true;*/
        if (o == null || getClass() != o.getClass())
            return false;

        LocalMedia albumBean = (LocalMedia) o;
        return title.equals(albumBean.title) &&
                path.equals(albumBean.path) &&
                size == albumBean.size &&
                dateAdded == albumBean.dateAdded &&
                dateModified == albumBean.dateModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeLong(this.dateAdded);
        dest.writeLong(this.dateModified);
        dest.writeInt(this.mediaMimeType);
        dest.writeString(this.mimeType);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.duration);
        dest.writeInt(this.albumId);
        dest.writeValue(this.dateTaken);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeByte(this.isCrop ? (byte) 1 : (byte) 0);
        dest.writeString(this.cropPath);
        dest.writeString(this.compressPath);
        dest.writeByte(this.isCompressed ? (byte) 1 : (byte) 0);
    }

    public LocalMedia() {
    }

    protected LocalMedia(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.dateAdded = in.readLong();
        this.dateModified = in.readLong();
        this.mediaMimeType = in.readInt();
        this.mimeType = in.readString();
        this.isSelected = in.readByte() != 0;
        this.duration = in.readInt();
        this.albumId = in.readInt();
        this.dateTaken = (Long) in.readValue(Long.class.getClassLoader());
        this.width = in.readInt();
        this.height = in.readInt();
        this.isCrop = in.readByte() != 0;
        this.cropPath = in.readString();
        this.compressPath = in.readString();
        this.isCompressed = in.readByte() != 0;
    }

    public static final Creator<LocalMedia> CREATOR = new Creator<LocalMedia>() {
        @Override
        public LocalMedia createFromParcel(Parcel source) {
            return new LocalMedia(source);
        }

        @Override
        public LocalMedia[] newArray(int size) {
            return new LocalMedia[size];
        }
    };
}


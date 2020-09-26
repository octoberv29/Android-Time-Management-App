package com.example.android.timemanagementapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "tasks_table")
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created_at")
    private Long created_at;

    @ColumnInfo(name = "completed")
    private Boolean completed;

    @ColumnInfo(name = "priority")
    private Boolean priority;

    @ColumnInfo(name = "due_date")
    private Long due_date;

    @ColumnInfo(name = "reminder")
    private Boolean reminder;

    @ColumnInfo(name = "remind_at")
    private Long remind_at;

    public Task(String title, String description, Long created_at, Boolean completed, Boolean priority, Long due_date, Boolean reminder, Long remind_at) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.completed = completed;
        this.priority = priority;
        this.due_date = due_date;
        this.reminder = reminder;
        this.remind_at = remind_at;
    }

    @Ignore // meaning don't use this constructor when constructing tasks db
    public Task() {
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            created_at = null;
        } else {
            created_at = in.readLong();
        }
        byte tmpCompleted = in.readByte();
        completed = tmpCompleted == 0 ? null : tmpCompleted == 1;
        byte tmpPriority = in.readByte();
        priority = tmpPriority == 0 ? null : tmpPriority == 1;
        if (in.readByte() == 0) {
            due_date = null;
        } else {
            due_date = in.readLong();
        }
        byte tmpReminder = in.readByte();
        reminder = tmpReminder == 0 ? null : tmpReminder == 1;
        if (in.readByte() == 0) {
            remind_at = null;
        } else {
            remind_at = in.readLong();
        }
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Long getDue_date() {
        return due_date;
    }

    public void setDue_date(Long due_date) {
        this.due_date = due_date;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public Long getRemind_at() {
        return remind_at;
    }

    public void setRemind_at(Long remind_at) {
        this.remind_at = remind_at;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", completed=" + completed +
                ", priority=" + priority +
                ", due_date=" + due_date +
                ", reminder=" + reminder +
                ", remind_at=" + remind_at +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        if (created_at == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(created_at);
        }
        dest.writeByte((byte) (completed == null ? 0 : completed ? 1 : 2));
        dest.writeByte((byte) (priority == null ? 0 : priority ? 1 : 2));
        if (due_date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(due_date);
        }
        dest.writeByte((byte) (reminder == null ? 0 : reminder ? 1 : 2));
        if (remind_at == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(remind_at);
        }
    }
}

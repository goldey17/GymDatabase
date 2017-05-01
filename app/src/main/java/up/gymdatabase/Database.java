package up.gymdatabase;

import android.provider.BaseColumns;

/**
 * This class defines the database schema and contract.
 * Created by Jordan Goldey on 4/3/2017.
 */
public final class Database {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Database() {}

    /* Inner class that defines the equipment table */
    public static class Equipment implements BaseColumns {
        public static final String TABLE_NAME = "Equipment";
        public static final String COLUMN_NAME_Equipment_ID = "EquipmentID";
        public static final String COLUMN_NAME_Equipment_Type = "EquipmentType";
        public static final String COLUMN_NAME_Equipment_Quantity = "EquipmentQuantity";
        public static final String COLUMN_NAME_Equipment_Rental_Cost = "EquipmentRentalCost";
    }

    /* Inner class that defines the rents table */
    public static class Rents implements BaseColumns {
        public static final String TABLE_NAME = "Rents";
        public static final String COLUMN_NAME_Equipment_ID = "EquipmentID";
        public static final String COLUMN_NAME_Student_ID = "StudentID";
        public static final String COLUMN_NAME_Equipment_Quantity = "EquipmentQuantity";
        public static final String COLUMN_NAME_Days = "Days";
    }
    public static class Classes implements BaseColumns {
        public static final String TABLE_NAME_CLASS = "Classes";
        public static final String COLUMN_NAME_Class_ID = "ClassID";
        public static final String COLUMN_NAME_Class_Name = "ClassName";
        public static final String COLUMN_NAME_Class_Time = "ClassTime";
        public static final String COLUMN_NAME_Class_Date = "ClassDate";
        public static final String COLUMN_NAME_Class_Location = "ClassLocation";
    }

    public static class Staff implements BaseColumns {
        public static final String TABLE_NAME = "Staff";
        public static final String COLUMN_NAME_Staff_ID = "StaffID";
        public static final String COLUMN_NAME_Staff_Name = "StaffName";
        public static final String COLUMN_NAME_Staff_Position = "StaffPosition";
    }

    public static class Teaches implements BaseColumns {
        public static final String TABLE_NAME = "Teaches";
        public static final String COLUMN_NAME_Class_ID = "ClassID";
        public static final String COLUMN_NAME_Staff_ID = "StaffID";
    }
    public static class Students implements BaseColumns {
        public static final String TABLE_NAME = "Students";
        public static final String COLUMN_NAME_Student_Name = "StudentName";
        public static final String COLUMN_NAME_Student_Year = "StudentYear";
        public static final String COLUMN_NAME_Student_ID = "StudentID";
    }

}

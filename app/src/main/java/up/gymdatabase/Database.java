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


}
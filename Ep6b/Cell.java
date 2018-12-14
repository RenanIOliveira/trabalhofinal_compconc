public class Cell{
    public static final int
            DOOR_OPENED    = 1,
            DOOR_CLOSED   = 2;


    int door;
    int id;
    
    public Cell(int id){
      this.door=DOOR_CLOSED;
      this.id = id;
    }

    public  void openDoor() {
        this.door = DOOR_OPENED;
        System.out.println("Cell number "+this.id+" opened");
    }

    public void closeDoor() {
        this.door = DOOR_CLOSED;
        System.out.println("Cell number "+this.id+" closed");
    }

    public boolean isTheDoorClosed(){
        return DOOR_CLOSED==this.door;
    }

  
}

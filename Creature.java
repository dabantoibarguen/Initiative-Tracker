public  class Creature {
    private String initiative;
    private String creatureName;
    private String hitpoints;

    public Creature(String init, String cName, String hp) {
        this.initiative = init;
        this.creatureName = cName;
        this.hitpoints = hp;
    }

    public String getCName() {
        return creatureName;
    }
        
    public String getInitiative() {
        return initiative;
    }

    public String getHp() {
        return hitpoints;
    }

    public void modifyHP(int n){
        int tot = (Integer.parseInt(this.hitpoints) + n);
        this.hitpoints = Integer.toString(Math.max(tot, 0));
        System.out.println(tot);
    }

}

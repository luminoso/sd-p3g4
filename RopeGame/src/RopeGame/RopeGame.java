package RopeGame;

/**
 * General description: This class starts the Rope Game.
 * It instantiates all the active and passive entities.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {

    public static void main(String[] args) throws InterruptedException {

        Thread cb, pg, rs, gr,
                c11, c12, c13, c14, c15,
                c21, c22, c23, c24, c25,
                c1, c2,
                rf;

        cb = new Thread(new Runnable() {
            public void run() {
                ServerSide.ServerRopeGame.main(new String[]{"CB"});
            }
        });
        
        pg = new Thread(new Runnable() {
            public void run() {
                ServerSide.ServerRopeGame.main(new String[]{"PG"});
            }
        });
        
        rs = new Thread(new Runnable() {
            public void run() {
                ServerSide.ServerRopeGame.main(new String[]{"RS"});
            }
        });
        
        gr = new Thread(new Runnable() {
            public void run() {
                ServerSide.ServerRopeGame.main(new String[]{"GR"});
            }
        });
        
        c11 = new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "1"});
            }
        });
        
        c12 = new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "2"});
            }
        });
        
        c13 = new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "3"});
            }
        });
        
        c14 = new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "4"});
            }
        });
        
        c15= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "5"});
            }
        });
        
        c21= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "1"});
            }
        });
        
        c22= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "2"});
            }
        });
        
        c23= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "3"});
            }
        });
        
        c24= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "4"});
            }
        });
        
        c25= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "5"});
            }
        });
        
        c1= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CH", "1", "0"});
            }
        });
        
        c2= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"CH", "2", "1"});
            }
        });
        
        rf= new Thread(new Runnable() {
            public void run() {
                ClientSide.ClientRopeGame.main(new String[]{"RF"});
            }
        });

        cb.setName("rp-CB"); cb.start();
        pg.setName("rp-PG"); pg.start();
        rs.setName("rp-RS"); rs.start();
        gr.setName("rp-GR"); gr.start();
        c11.setName("rp-c11"); c11.start();
        c12.setName("rp-C12"); c12.start();
        c13.setName("rp-c13"); c13.start();
        c14.setName("rp-c14"); c14.start();
        c15.setName("rp-c15"); c15.start();
        c21.setName("rp-c21"); c21.start();
        c22.setName("rp-c22"); c22.start();
        c23.setName("rp-c23"); c23.start();
        c24.setName("rp-c24"); c24.start();
        c25.setName("rp-c25"); c25.start();
        c1.setName("rp-c1"); c1.start();
        c2.setName("rp-c2"); c2.start();
        rf.setName("rp-rf"); rf.start();
        
        rf.join();
        
        Thread[] threads = new Thread[]{cb, pg, rs, gr,
                c11, c12, c13, c14, c15,
                c21, c22, c23, c24, c25,
                c1, c2};
        
        for(Thread t : threads){
            while(t.isAlive())
                t.interrupt();
        }

    }

    /**
     * Function to generate a random strength when a player is instantiated.
     *
     * @return a strength for a player instantiation
     */
    private static int randomStrength() {
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }
}

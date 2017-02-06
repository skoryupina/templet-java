package newtemplet;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import newtemplet.akka.ActorBase;
import newtemplet.akka.Engine;
import newtemplet.akka.MessageBase;

import java.util.Random;


public class Main {
    public int id;

    public static final int SCALE = 1500;

    public static final int W = SCALE * 2;
    public static final int H = SCALE;
    public static final int T = SCALE * 2;
    public static final int N = H - 2;

    public static double[][] field = new double[H][W];
    public static double[][] field1 = new double[H][W];

    public static final int OBS_N = 19;

    double obs_seq[];
    double seq_max, seq_mid, seq_min;

    double obs_omp[];
    double omp_max, omp_mid, omp_min;

    double obs_tet[];
    double tet_max, tet_mid, tet_min;

    public static Engine engine = new Engine();
    public static ActorRef actors[] = new ActorRef[N];
    public static MessageBase messages[] = new MessageBase[N - 1];
    public static int time[] = new int[N];
    final static ActorSystem system = ActorSystem.create("MySystem");


    public static void shufle() {
        Random random = new Random();
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                double srand = random.nextDouble();
                field[i][j] = srand;
                field1[i][j] = srand;
            }
        }
    }

    static double seq_alg() {
        double time = System.currentTimeMillis();

        for (int t = 1; t <= T; t++)
            for (int i = 1; i < H - 1; i++) operation(i);

        return System.currentTimeMillis() - time;
    }

    boolean compare() {
        for (int i = 0; i < H; i++)
            for (int j = 0; j < W; j++)
                if (field1[i][j] != field[i][j]) return false;
        return true;
    }

    void min_max() {
        seq_max = seq_min = obs_seq[0];
        omp_max = omp_min = obs_omp[0];
        tet_max = tet_min = obs_tet[0];

        for (int i = 1; i < OBS_N; i++) {
            seq_max = obs_seq[i] > seq_max ? obs_seq[i] : seq_max;
            seq_min = obs_seq[i] < seq_min ? obs_seq[i] : seq_min;

            omp_max = obs_omp[i] > omp_max ? obs_omp[i] : omp_max;
            omp_min = obs_omp[i] < omp_min ? obs_omp[i] : omp_min;

            tet_max = obs_tet[i] > tet_max ? obs_tet[i] : tet_max;
            tet_min = obs_tet[i] < tet_min ? obs_tet[i] : tet_min;
        }
        seq_mid = (seq_min + seq_max) / 2;
        omp_mid = (omp_min + omp_max) / 2;
        tet_mid = (tet_min + tet_max) / 2;
    }

    public static void operation(int i) {
        for (int j = 1; j < W - 1; j++) {
            field[i][j] = (field[i][j - 1] + field[i][j + 1] + field[i - 1][j] + field[i + 1][j]) * 0.25;
        }
    }

    static double par_tet() {
        for (int i = 0; i < N; i++) {
            actors[i] = system.actorOf(Props.create(ActorBase.class, i));
            time[i] = 1;
        }
        for (int i = 0; i < N - 1; i++) {
            messages[i] = new MessageBase();
            messages[i].setActor(actors[i]);
            messages[i].setSending(false);
        }
        MessageBase.send(engine, messages[0], actors[0]);

        return engine.run();
    }

    public static void main(String[] args) {
        shufle();
        seq_alg();
        System.out.println(par_tet());
        System.out.println("Print");

//            for (int i = 0; i < OBS_N; i++){
//                shufle_seq();	obs_seq[i] = seq_alg();
//                shufle();	obs_omp[i] = par_omp(); cout << (compare() ? "OMP Ok " : "something wrong in OMP ");
//                shufle();	obs_tet[i] = par_tet(); cout << (compare() ? "Templet Ok " : "something wrong in Templet ");
//                cout << (int)((float)(i+1)/OBS_N*100) << "% done" << endl;
//            }
//
//            min_max();
//
//            cout << "\nTest results for H = " << H << "; W = " << W << "; T = " << T << "; OMP max threads  = " << omp_get_max_threads() << "\n";
//
//            cout << "\nseq_min  = " << seq_min << " sec; " << "\nseq_mid  = " << seq_mid <<" sec; \nseq_max  = " << seq_max << " sec\n";
//            cout << "\nomp_min  = " << omp_min << " sec; " << "\nomp_mid  = " << omp_mid << " sec; \nomp_max  = " << omp_max << " sec\n";
//            cout << "\ntet_min  = " << tet_min << " sec; " << "\ntet_mid  = " << tet_mid << " sec; \ntet_max  = " << tet_max << " sec\n";
    }


}
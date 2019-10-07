import java.util.*;
import java.io.*;

//Denna klass ärver Global så att man kan använda time och signalnamnen utan punktnotation


public class MainSimulation extends Global {

    public static void main(String[] args) throws IOException {

        //Signallistan startas och actSignal deklareras. actSignal är den senast utplockade signalen i huvudloopen nedan.
        Signal actSignal;
        new SignalList();

        //Här nedan skapas de processinstanser som behövs och parametrar i dem ges värden.

        QS Q1 = new QS();
        QS Q2 = new QS();
        QS Q3 = new QS();
        QS Q4 = new QS();
        QS Q5 = new QS();

        Q1.sendTo = null;
        Q2.sendTo = null;
        Q3.sendTo = null;
        Q4.sendTo = null;
        Q5.sendTo = null;

        Gen Generator = new Gen();
        Generator.lambda = 45; //Generator ska generera nio kunder per sekund
//        Generator.sendTo = Q1; //De genererade kunderna ska skickas till kösystemet QS

        //Här nedan skickas de första signalerna för att simuleringen ska komma igång.

        SignalList.SendSignal(READY, Generator, time);

        SignalList.SendSignal(MEASURE, Q1, time);
        SignalList.SendSignal(MEASURE, Q2, time);
        SignalList.SendSignal(MEASURE, Q3, time);
        SignalList.SendSignal(MEASURE, Q4, time);
        SignalList.SendSignal(MEASURE, Q5, time);


        // Detta är simuleringsloopen:
        int n = 1;

        while (time < 100000) {

//            Random random = new Random();
//            int n = 1 + random.nextInt(5);

            /*switch (n) {
                case 1:
                    Generator.sendTo = Q1;
                    n = 2;
                    break;
                case 2:
                    Generator.sendTo = Q2;
                    n = 3;
                    break;
                case 3:
                    Generator.sendTo = Q3;
                    n = 4;
                    break;
                case 4:
                    Generator.sendTo = Q4;
                    n = 5;
                    break;
                case 5:
                    Generator.sendTo = Q5;
                    n = 1;
                    break;
            }*/
            Generator.sendTo = findLeastJob(Q1, Q2, Q3, Q4, Q5);
            actSignal = SignalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        //Slutligen skrivs resultatet av simuleringen ut nedan:
        System.out.println("Medelantal kunder i kö 1: " + 1.0 * Q1.accumulated / Q1.noMeasurements);
        System.out.println("Medelantal kunder i kö 2: " + 1.0 * Q2.accumulated / Q2.noMeasurements);
        System.out.println("Medelantal kunder i kö 3: " + 1.0 * Q3.accumulated / Q3.noMeasurements);
        System.out.println("Medelantal kunder i kö 4: " + 1.0 * Q4.accumulated / Q4.noMeasurements);
        System.out.println("Medelantal kunder i kö 5: " + 1.0 * Q5.accumulated / Q5.noMeasurements);
    }

    private static QS findLeastJob(QS q1, QS q2, QS q3, QS q4, QS q5) {
        List<QS> list = new ArrayList<>();
        list.add(q1);
        list.add(q2);
        list.add(q3);
        list.add(q4);
        list.add(q5);
        QS withLeastJob = q1;
        for (QS qs : list) {
        	if (qs.numberInQueue < withLeastJob.numberInQueue) {
        		withLeastJob = qs;
			}
        }
        return withLeastJob;
    }
}
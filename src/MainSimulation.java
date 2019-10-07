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

		Q1.sendTo = Q2;
        Q2.sendTo = Q3;
        Q3.sendTo = null;

        Gen Generator = new Gen();
        Generator.lambda = 8; //Generator ska generera nio kunder per sekund
        Generator.sendTo = Q1; //De genererade kunderna ska skickas till kösystemet QS

        //Här nedan skickas de första signalerna för att simuleringen ska komma igång.

        SignalList.SendSignal(READY, Generator, time);

        SignalList.SendSignal(MEASURE, Q1, time);
		SignalList.SendSignal(MEASURE, Q2, time);
		SignalList.SendSignal(MEASURE, Q3, time);


        // Detta är simuleringsloopen:

        while (time < 100000) {
            actSignal = SignalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        //Slutligen skrivs resultatet av simuleringen ut nedan:

        System.out.println("Medelantal kunder i kö 1: " + 1.0 * Q1.accumulated / Q1.noMeasurements);
		System.out.println("Medelantal kunder i kö 2: " + 1.0 * Q2.accumulated / Q2.noMeasurements);
		System.out.println("Medelantal kunder i kö 3: " + 1.0 * Q3.accumulated / Q3.noMeasurements);

    }
}
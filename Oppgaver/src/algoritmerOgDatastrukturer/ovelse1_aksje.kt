package algoritmerOgDatastrukturer

import com.xeiam.xchart.QuickChart
import com.xeiam.xchart.SwingWrapper
import java.util.stream.IntStream

fun main(args: Array<String>) {
    val stockChanges = IntStream.generate { Math.round(10 - Math.random() * 20).toInt() }.limit(20).toArray()  //arrayOf(-6,4,0,-3,-9,0,4,1,-2) 4-7   arrayOf(-1,3,-9,2,2,-1,2,-1,-5)
    val stockChange = StockChange(stockChanges)

    println("For stock changes $stockChanges the best day for buying is ${stockChange.bestBuyDate} and best selling day is ${stockChange.bestSellDate}")


    showGraph(data = DoubleArray(stockChanges.size) { stockChange.dailyPrice[it].toDouble() }, funName = "Best days = [${stockChange.bestBuyDate}  ${stockChange.bestSellDate}]" )

}

fun showGraph(data: DoubleArray, title: String = "Aksjepris", xTitle: String = "Dag", yTitle: String = "Pris", funName: String = "Data") {
    SwingWrapper(QuickChart.getChart(title, xTitle, yTitle, funName, DoubleArray(data.size){ (it).toDouble() }, data)).displayChart()
}

class Timer() {
    private var times = ArrayList<Long>()
    private var started = false
    private var startTime: Long = 0

    fun start() {
        if(!started) {
            started = true
            startTime = System.currentTimeMillis()
        }
    }

    fun end() {
        if(started) {
            started = false
            times.add(System.currentTimeMillis() - startTime)
        }
    }

    fun totalTime() = times.sum()
}


class StockChange(val data: IntArray) {
    val bestBuyDate: Int
    val bestSellDate: Int
    val dailyPrice: IntArray by lazy {
        IntArray(data.size) {
            IntStream.rangeClosed(0,it)
                    .map { index -> data[index] }
                    .sum()
        }
    }

    init {
        var bestSum = 0
        var bestBuy = 0
        var bestSell = 0

        var sum = 0
        var bottomIndex = 0

        for (i in 1 until data.size) {
            if(data[i-1] < 0 && data[i] >= 0) { // Bunn
                if(bestSum + data[i-1] > 0) {
                    sum = bestSum + data[i-1] + data[i]
                } else {
                    bottomIndex = i
                    sum = data[i]
                }
            }
            else if(data[i] > 0) sum += data[i] // Bakke opp
            else if(data[i-1] > 0 && data[i] <= 0) { // Topp
                if(sum > bestSum) {
                    bestSum = sum
                    bestBuy = bottomIndex - 1
                    bestSell = i-1
                }
                sum = data[i]
            }
            else if(data[i] < 0) { // Bakke ned
                sum += data[i]
            }
        }
        bestBuyDate = bestBuy
        bestSellDate = bestSell
    }
}
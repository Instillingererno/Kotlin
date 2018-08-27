package algoritmerOgDatastrukturer

import com.xeiam.xchart.QuickChart
import com.xeiam.xchart.SwingWrapper
import java.util.stream.IntStream

fun main(args: Array<String>) {
    val stockChanges = intArrayOf(-1,3,-9,2,2,-1,2,-1,-5) // arrayOf(-6,4,0,-3,-9,0,4,1,-2) 4-7  IntStream.generate { Math.round(10 - Math.random() * 20).toInt() }.limit(40).toArray()
    val stockChange = StockChange(stockChanges)

    var runder = 0
    val startTid = System.currentTimeMillis()
    do {
        runder++
        val temp = StockChange(stockChanges)
    } while (System.currentTimeMillis() - startTid < 1000)
    val sluttTid = System.currentTimeMillis()

    println("""
        Målte runder var: $runder på ${sluttTid - startTid}ms
        Som gir en estimert tid på ${(sluttTid-startTid).toDouble() / runder.toDouble()}ms per runde
        n er i denne gjennomkjøringen lik: ${stockChanges.size}

        kompleksiteten burde være O(n)
        n lik 40 har gitt tiden 10.4360 E-5 ms
        n lik 30 har gitt tiden  7.3424 E-5 ms
        n lik 20 har gitt tiden  5.2349 E-5 ms
        n lik 10 har gitt tiden  3.8327 E-5 ms

    """.trimIndent())

    showGraph(data = DoubleArray(stockChanges.size) { stockChange.dailyPrice[it].toDouble() }, funName = "Best days = [${stockChange.bestBuyDate}  ${stockChange.bestSellDate}]" )

}

fun showGraph(data: DoubleArray, title: String = "Aksjepris", xTitle: String = "Dag", yTitle: String = "Pris", funName: String = "Data") {
    SwingWrapper(QuickChart.getChart(title, xTitle, yTitle, funName, DoubleArray(data.size){ (it).toDouble() }, data)).displayChart()
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
                    bestBuy = if(bottomIndex == 0) 0 else bottomIndex - 1
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
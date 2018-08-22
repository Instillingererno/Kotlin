package algoritmerOgDatastrukturer

import java.util.stream.IntStream

fun main(args: Array<String>) {
    val stockChanges = arrayOf(-6,4,0,-3,-9,0,4,1,-2) // 4-7   arrayOf(-1,3,-9,2,2,-1,2,-1,-5)
    val stockChange = StockChange(stockChanges)

    println("For stock changes $stockChanges the best day for buying is ${stockChange.bestBuyDate} and best selling day is ${stockChange.bestSellDate}")
}

class StockChange(val data: Array<Int>) {
    val bestBuyDate: Int
    val bestSellDate: Int

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
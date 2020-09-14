package groovy

class CalculateMax {

    public getMax(List<Integer> numbers) {
        int max = 0;
        numbers.each { n -> if (max < n) { max = n }}
        return max;
    }
}
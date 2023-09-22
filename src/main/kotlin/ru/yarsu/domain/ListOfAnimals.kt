package ru.yarsu.domain

data class ListOfAnimals(private var listOfAnimals: MutableList<Animal>) {

    fun add(animal: Animal) {
        listOfAnimals.add(animal)
    }

    fun fetchAnimal(i: Int): Animal = listOfAnimals[i]

    fun fetchAllAnimals(): MutableList<Animal> = listOfAnimals

    fun editAnimal(currentAnimal: Animal, newAnimal: Animal) {
        val index = listOfAnimals.indexOf(currentAnimal)
        listOfAnimals[index] = newAnimal
    }

    fun deleteAnimal(currentAnimal: Animal) {
        listOfAnimals.remove(currentAnimal)
    }

    fun getListOfTypes(): MutableList<String> {
        val listOfTypes = mutableListOf<String>()
        for (animal in listOfAnimals) {
            if (animal.type in listOfTypes) {
                continue
            }
            listOfTypes.add(animal.type)
        }
        return listOfTypes
    }

    fun sorting() {
        listOfAnimals.sortWith(compareBy(Animal::type).thenByDescending(Animal::datetime))
        for (animal in listOfAnimals) {
            animal.setIndex(listOfAnimals.indexOf(animal))
        }
    }

    fun filterByParameters(filterType: String, startYear: Int, endYear: Int): List<Animal> {
        if (filterType in getListOfTypes()) {
            return listOfAnimals.filter { animal ->
                val animalYear = animal.datetime.year
                animal.type == filterType && animalYear in startYear..endYear
            }
        }
        return listOfAnimals.filter { animal ->
            val animalYear = animal.datetime.year
            animalYear in startYear..endYear
        }
    }

    fun getBoundsYears(): IntArray {
        var minYear = listOfAnimals[0].datetime.year
        var maxYear = listOfAnimals[0].datetime.year
        for (animal in listOfAnimals) {
            if (animal.datetime.year < minYear) {
                minYear = animal.datetime.year
            }
            if (animal.datetime.year > maxYear) {
                maxYear = animal.datetime.year
            }
        }
        return intArrayOf(minYear, maxYear)
    }

    fun checkIndex(index: Int): Boolean {
        return index in 0..listOfAnimals.lastIndex
    }
}

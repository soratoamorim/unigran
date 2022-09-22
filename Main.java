import java.util.Arrays;

class Main 
{
    public enum Gender
    {
        MALE, FEMALE
    }

    public interface IMCData
    {
        float getWeight();
        float getHeight();
        Gender getGender();
    }

    public class IMC
    {
        private float imc;
        private Gender gender;
        private IMCWeightRange category;

        public IMC(float imc, Gender g)
        {
            this.imc = imc;
            this.gender = g;
            this.category = IMCWeightRange.getCategory(imc, gender);
        }

        public float getValue() { return imc; }
        public Gender getGender() { return gender; }
        public IMCWeightRange getCategory() { return category; }
    }

    public class IMCCalculator
    {
        public IMC calculate(IMCData data)
        {
            float nom = data.getWeight();
            float denom = (float) Math.pow(data.getHeight(), 2);
            float imc = nom / denom;
            return new IMC(imc, data.getGender());
        }
    }

    public enum IMCWeightRange
    {
        UNDERWEIGHT(20.f),
        NORMAL_WEIGHT(25.f),
        LIGHT_OBESITY(30.f),
        MODERATE_OBESITY(40.f),
        MORBID_OBESITY(Float.MAX_VALUE);

        private float maxIndex;

        private IMCWeightRange(float maxIndex) { this.maxIndex = maxIndex; }

        public float getMaxIndex() { return maxIndex; }

        public static IMCWeightRange getCategory(float index, Gender g)
        {
            float imc = g == Gender.FEMALE ? index - 1 : index;
            return Arrays
                .stream(IMCWeightRange.values())
                .filter(x -> imc < x.getMaxIndex())
                .findFirst()
                .get();
        }
    }

    public class IMCCategoryPrinter
    {
        public String getMessageWithIMC(IMC imc)
        {
            return getMessage(imc.getCategory()) + " [IMC: " + imc.getValue() + "]";
        }

        public String getMessage(IMCWeightRange category)
        {
            switch(category)
            {
                case UNDERWEIGHT:
                    return "Você está abaixo do peso";
                case NORMAL_WEIGHT:
                    return "Você está com peso normal";
                case LIGHT_OBESITY:
                    return "Você está com uma obesidade leve";
                case MODERATE_OBESITY:
                    return "Você está com uma obesidade moderada";
                case MORBID_OBESITY:
                    return "Você está com obesidade mórbida";
            }

            return "Não foi possível encontrar mensagem para categoria: " + category.toString();
        }
    }

    public class Person implements IMCData
    {
        private String name;
        private int age;

        private float weight;
        private float height;

        private Gender gender;

        public Person(String name, int age, float weight, float height, Gender gender)
        {
            this.name   = name;
            this.age    = age;
            this.weight = weight;
            this.height = height;
            this.gender = gender;
        }

        public String   getName()   { return name;      }
        public int      getAge()    { return age;       }
        public float    getWeight() { return weight;    }
        public float    getHeight() { return height;    }
        public Gender   getGender() { return gender;    }
    }

    public void runApp()
    {
        System.out.println("Hello world!");
        Person me = new Person("LHC", 24, 62, 1.70f, Gender.MALE);
        Person sedex = new Person("Idervan", 28, 70.f, 1.75f, Gender.MALE);

        printPersonIMC(me);
        System.out.println();
        printPersonIMC(sedex);

        System.out.println("Os IMCs calculados levam em consideração também o seu sexo!");	
    }

    public void printPersonIMC(Person p)
    {
        System.out.println("Olá " + p.getName() + " você tem " + p.getAge() + " anos.");

        IMC myIMC = new IMCCalculator().calculate(p);
        System.out.println(new IMCCategoryPrinter().getMessageWithIMC(myIMC));
    }

    public static void main(String[] args)
    {
        Main m = new Main();
        m.runApp();
    }
}
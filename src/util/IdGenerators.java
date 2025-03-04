package util;

public class IdGenerators {

    public static IdGenerator createGenerator(String sequenceFileName) {
        return new IdGeneratorFileBased(sequenceFileName);
    }
}


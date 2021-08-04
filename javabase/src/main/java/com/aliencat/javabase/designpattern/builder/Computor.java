package com.aliencat.javabase.designpattern.builder;

/**
 * 使用建造者模式创建一台电脑
 */
public class Computor {

    private String cpu;
    private String screen;
    private String memory;
    private String mainboard;
    private String mouse;
    private String keyboard;

    public Computor() {
        throw new RuntimeException("材料不足，无法建造");
    }

    private Computor(Builder builder) {
        mainboard = builder.mainboard;
        cpu = builder.cpu;
        memory = builder.memory;
        screen = builder.screen;
        mouse = builder.mouse;
        keyboard = builder.keyboard;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Computor{" +
                "cpu='" + cpu + '\'' +
                ", screen='" + screen + '\'' +
                ", memory='" + memory + '\'' +
                ", mainboard='" + mainboard + '\'' +
                ", mouse='" + mouse + '\'' +
                ", keyboard='" + keyboard + '\'' +
                '}';
    }

    public static final class Builder {

        private String cpu;
        private String screen;
        private String memory;
        private String mainboard;
        private String mouse;
        private String keyboard;

        public Builder() {
        }


        public Builder mouse(String mouse) {
            this.mouse = mouse;
            return this;
        }


        public Builder keyboard(String keyboard) {
            this.keyboard = keyboard;
            return this;
        }

        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder screen(String screen) {
            this.screen = screen;
            return this;
        }

        public Builder memory(String memory) {
            this.memory = memory;
            return this;
        }

        public Builder mainboard(String mainboard) {
            this.mainboard = mainboard;
            return this;
        }

        public Computor build() {
            return new Computor(this);
        }
    }

}

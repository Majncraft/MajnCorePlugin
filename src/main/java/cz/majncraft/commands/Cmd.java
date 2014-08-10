package cz.majncraft.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cmd {
	String[] commands();
	String permission() default "";
	boolean permissionReverse() default false;
	boolean opHave() default true;
	public enum Side { Client, Server, Both}
	Side side() default Side.Both;
}

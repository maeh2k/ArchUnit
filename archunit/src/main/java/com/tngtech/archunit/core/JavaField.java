package com.tngtech.archunit.core;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaField extends JavaMember<Field, MemberDescription.ForField> {
    private final TypeDetails type;
    private Supplier<Set<JavaFieldAccess>> accessesToSelf = Suppliers.ofInstance(Collections.<JavaFieldAccess>emptySet());

    private JavaField(Builder builder) {
        super(builder.owner, builder.member.getAnnotations(), builder.member.getName(), builder.member.getDescriptor(), JavaModifier.getModifiersFor(builder.member.getModifiers()));
        type = builder.member.getType();
    }

    @Override
    public String getFullName() {
        return getOwner().getName() + "." + getName();
    }

    public TypeDetails getType() {
        return type;
    }

    @Override
    public Set<JavaFieldAccess> getAccessesToSelf() {
        return accessesToSelf.get();
    }

    void registerAccessesToField(Supplier<Set<JavaFieldAccess>> accesses) {
        this.accessesToSelf = checkNotNull(accesses);
    }

    public static DescribedPredicate<JavaField> hasType(DescribedPredicate<? super TypeDetails> predicate) {
        return predicate.onResultOf(GET_TYPE)
                .as("has type " + predicate.getDescription());
    }

    public static final Function<JavaField, TypeDetails> GET_TYPE = new Function<JavaField, TypeDetails>() {
        @Override
        public TypeDetails apply(JavaField input) {
            return input.getType();
        }
    };

    static final class Builder extends JavaMember.Builder<MemberDescription.ForField, JavaField> {
        @Override
        public JavaField build(JavaClass owner) {
            this.owner = owner;
            return new JavaField(this);
        }

        BuilderWithBuildParameter<JavaClass, JavaField> withField(Field field) {
            return withMember(new MemberDescription.ForDeterminedField(field));
        }
    }
}

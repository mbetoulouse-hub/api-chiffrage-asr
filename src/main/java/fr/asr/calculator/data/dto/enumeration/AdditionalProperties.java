package fr.asr.calculator.data.dto.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdditionalProperties {
    CUSTOM_CADENCE(true),
    INT_INPUT_CONTAINERS(true),
    CLEANING_X_TIMES(true),
    CHECK_BOX_ARMCHAIR(true),
    CHECK_BOX_ACTIVITY(true),
    CHECK_BOX_PARTIAL(true),
    AUTOLAVEUSE(true),
    CHECK_BOX_PARKING(true),
    SELECT_WEIGHTS_SPECIFIC(true),
    SELECT_CARDIO_SPECIFIC(true),
    SELECT_KITCHEN_CLEAN_TYPE(true),
    SELECT_RESTROOM_CLEAN_TYPE(true),
    CUSTOM_WEEK_NUMBER(false),
    ;

    private final boolean modifyCadenceStrategy;
}

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/contract/common.proto

package org.tron.protos.contract;

public final class Common {
  private Common() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code protocol.ResourceCode}
   */
  public enum ResourceCode
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>BANDWIDTH = 0;</code>
     */
    BANDWIDTH(0),
    /**
     * <code>ENERGY = 1;</code>
     */
    ENERGY(1),
    /**
     * <code>TRON_POWER = 2;</code>
     */
    TRON_POWER(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>BANDWIDTH = 0;</code>
     */
    public static final int BANDWIDTH_VALUE = 0;
    /**
     * <code>ENERGY = 1;</code>
     */
    public static final int ENERGY_VALUE = 1;
    /**
     * <code>TRON_POWER = 2;</code>
     */
    public static final int TRON_POWER_VALUE = 2;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static ResourceCode valueOf(int value) {
      return forNumber(value);
    }

    public static ResourceCode forNumber(int value) {
      switch (value) {
        case 0: return BANDWIDTH;
        case 1: return ENERGY;
        case 2: return TRON_POWER;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ResourceCode>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        ResourceCode> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ResourceCode>() {
            public ResourceCode findValueByNumber(int number) {
              return ResourceCode.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return org.tron.protos.contract.Common.getDescriptor().getEnumTypes().get(0);
    }

    private static final ResourceCode[] VALUES = values();

    public static ResourceCode valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private ResourceCode(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:protocol.ResourceCode)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\032core/contract/common.proto\022\010protocol*9" +
      "\n\014ResourceCode\022\r\n\tBANDWIDTH\020\000\022\n\n\006ENERGY\020" +
      "\001\022\016\n\nTRON_POWER\020\002BE\n\030org.tron.protos.con" +
      "tractZ)github.com/tronprotocol/grpc-gate" +
      "way/coreb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: api.proto

#define INTERNAL_SUPPRESS_PROTOBUF_FIELD_DEPRECATION
#include "api.pb.h"

#include <algorithm>

#include <google/protobuf/stubs/common.h>
#include <google/protobuf/stubs/port.h>
#include <google/protobuf/stubs/once.h>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/wire_format_lite_inl.h>
#include <google/protobuf/descriptor.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/reflection_ops.h>
#include <google/protobuf/wire_format.h>
// @@protoc_insertion_point(includes)

namespace ch {
namespace sharpsoft {
namespace hexapod {
namespace transfer {

namespace {

const ::google::protobuf::Descriptor* IMU_descriptor_ = NULL;
const ::google::protobuf::internal::GeneratedMessageReflection*
  IMU_reflection_ = NULL;

}  // namespace


void protobuf_AssignDesc_api_2eproto() {
  protobuf_AddDesc_api_2eproto();
  const ::google::protobuf::FileDescriptor* file =
    ::google::protobuf::DescriptorPool::generated_pool()->FindFileByName(
      "api.proto");
  GOOGLE_CHECK(file != NULL);
  IMU_descriptor_ = file->message_type(0);
  static const int IMU_offsets_[10] = {
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, timestamp_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, ax_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, ay_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, az_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, gx_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, gy_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, gz_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, mx_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, my_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, mz_),
  };
  IMU_reflection_ =
    ::google::protobuf::internal::GeneratedMessageReflection::NewGeneratedMessageReflection(
      IMU_descriptor_,
      IMU::default_instance_,
      IMU_offsets_,
      -1,
      -1,
      -1,
      sizeof(IMU),
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, _internal_metadata_),
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(IMU, _is_default_instance_));
}

namespace {

GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AssignDescriptors_once_);
inline void protobuf_AssignDescriptorsOnce() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AssignDescriptors_once_,
                 &protobuf_AssignDesc_api_2eproto);
}

void protobuf_RegisterTypes(const ::std::string&) {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedMessage(
      IMU_descriptor_, &IMU::default_instance());
}

}  // namespace

void protobuf_ShutdownFile_api_2eproto() {
  delete IMU::default_instance_;
  delete IMU_reflection_;
}

void protobuf_AddDesc_api_2eproto() {
  static bool already_here = false;
  if (already_here) return;
  already_here = true;
  GOOGLE_PROTOBUF_VERIFY_VERSION;

  ::google::protobuf::DescriptorPool::InternalAddGeneratedFile(
    "\n\tapi.proto\022\035ch.sharpsoft.hexapod.transf"
    "er\"\204\001\n\003IMU\022\021\n\ttimestamp\030\001 \001(\003\022\n\n\002ax\030\002 \001("
    "\002\022\n\n\002ay\030\003 \001(\002\022\n\n\002az\030\004 \001(\002\022\n\n\002gx\030\005 \001(\002\022\n\n"
    "\002gy\030\006 \001(\002\022\n\n\002gz\030\007 \001(\002\022\n\n\002mx\030\010 \001(\002\022\n\n\002my\030"
    "\t \001(\002\022\n\n\002mz\030\n \001(\002b\006proto3", 185);
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedFile(
    "api.proto", &protobuf_RegisterTypes);
  IMU::default_instance_ = new IMU();
  IMU::default_instance_->InitAsDefaultInstance();
  ::google::protobuf::internal::OnShutdown(&protobuf_ShutdownFile_api_2eproto);
}

// Force AddDescriptors() to be called at static initialization time.
struct StaticDescriptorInitializer_api_2eproto {
  StaticDescriptorInitializer_api_2eproto() {
    protobuf_AddDesc_api_2eproto();
  }
} static_descriptor_initializer_api_2eproto_;

namespace {

static void MergeFromFail(int line) GOOGLE_ATTRIBUTE_COLD;
static void MergeFromFail(int line) {
  GOOGLE_CHECK(false) << __FILE__ << ":" << line;
}

}  // namespace


// ===================================================================

#if !defined(_MSC_VER) || _MSC_VER >= 1900
const int IMU::kTimestampFieldNumber;
const int IMU::kAxFieldNumber;
const int IMU::kAyFieldNumber;
const int IMU::kAzFieldNumber;
const int IMU::kGxFieldNumber;
const int IMU::kGyFieldNumber;
const int IMU::kGzFieldNumber;
const int IMU::kMxFieldNumber;
const int IMU::kMyFieldNumber;
const int IMU::kMzFieldNumber;
#endif  // !defined(_MSC_VER) || _MSC_VER >= 1900

IMU::IMU()
  : ::google::protobuf::Message(), _internal_metadata_(NULL) {
  SharedCtor();
  // @@protoc_insertion_point(constructor:ch.sharpsoft.hexapod.transfer.IMU)
}

void IMU::InitAsDefaultInstance() {
  _is_default_instance_ = true;
}

IMU::IMU(const IMU& from)
  : ::google::protobuf::Message(),
    _internal_metadata_(NULL) {
  SharedCtor();
  MergeFrom(from);
  // @@protoc_insertion_point(copy_constructor:ch.sharpsoft.hexapod.transfer.IMU)
}

void IMU::SharedCtor() {
    _is_default_instance_ = false;
  _cached_size_ = 0;
  timestamp_ = GOOGLE_LONGLONG(0);
  ax_ = 0;
  ay_ = 0;
  az_ = 0;
  gx_ = 0;
  gy_ = 0;
  gz_ = 0;
  mx_ = 0;
  my_ = 0;
  mz_ = 0;
}

IMU::~IMU() {
  // @@protoc_insertion_point(destructor:ch.sharpsoft.hexapod.transfer.IMU)
  SharedDtor();
}

void IMU::SharedDtor() {
  if (this != default_instance_) {
  }
}

void IMU::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const ::google::protobuf::Descriptor* IMU::descriptor() {
  protobuf_AssignDescriptorsOnce();
  return IMU_descriptor_;
}

const IMU& IMU::default_instance() {
  if (default_instance_ == NULL) protobuf_AddDesc_api_2eproto();
  return *default_instance_;
}

IMU* IMU::default_instance_ = NULL;

IMU* IMU::New(::google::protobuf::Arena* arena) const {
  IMU* n = new IMU;
  if (arena != NULL) {
    arena->Own(n);
  }
  return n;
}

void IMU::Clear() {
// @@protoc_insertion_point(message_clear_start:ch.sharpsoft.hexapod.transfer.IMU)
#if defined(__clang__)
#define ZR_HELPER_(f) \
  _Pragma("clang diagnostic push") \
  _Pragma("clang diagnostic ignored \"-Winvalid-offsetof\"") \
  __builtin_offsetof(IMU, f) \
  _Pragma("clang diagnostic pop")
#else
#define ZR_HELPER_(f) reinterpret_cast<char*>(\
  &reinterpret_cast<IMU*>(16)->f)
#endif

#define ZR_(first, last) do {\
  ::memset(&first, 0,\
           ZR_HELPER_(last) - ZR_HELPER_(first) + sizeof(last));\
} while (0)

  ZR_(timestamp_, mx_);
  ZR_(my_, mz_);

#undef ZR_HELPER_
#undef ZR_

}

bool IMU::MergePartialFromCodedStream(
    ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!GOOGLE_PREDICT_TRUE(EXPRESSION)) goto failure
  ::google::protobuf::uint32 tag;
  // @@protoc_insertion_point(parse_start:ch.sharpsoft.hexapod.transfer.IMU)
  for (;;) {
    ::std::pair< ::google::protobuf::uint32, bool> p = input->ReadTagWithCutoff(127);
    tag = p.first;
    if (!p.second) goto handle_unusual;
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
      // optional int64 timestamp = 1;
      case 1: {
        if (tag == 8) {
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &timestamp_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(21)) goto parse_ax;
        break;
      }

      // optional float ax = 2;
      case 2: {
        if (tag == 21) {
         parse_ax:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &ax_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(29)) goto parse_ay;
        break;
      }

      // optional float ay = 3;
      case 3: {
        if (tag == 29) {
         parse_ay:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &ay_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(37)) goto parse_az;
        break;
      }

      // optional float az = 4;
      case 4: {
        if (tag == 37) {
         parse_az:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &az_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(45)) goto parse_gx;
        break;
      }

      // optional float gx = 5;
      case 5: {
        if (tag == 45) {
         parse_gx:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &gx_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(53)) goto parse_gy;
        break;
      }

      // optional float gy = 6;
      case 6: {
        if (tag == 53) {
         parse_gy:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &gy_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(61)) goto parse_gz;
        break;
      }

      // optional float gz = 7;
      case 7: {
        if (tag == 61) {
         parse_gz:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &gz_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(69)) goto parse_mx;
        break;
      }

      // optional float mx = 8;
      case 8: {
        if (tag == 69) {
         parse_mx:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &mx_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(77)) goto parse_my;
        break;
      }

      // optional float my = 9;
      case 9: {
        if (tag == 77) {
         parse_my:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &my_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(85)) goto parse_mz;
        break;
      }

      // optional float mz = 10;
      case 10: {
        if (tag == 85) {
         parse_mz:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &mz_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectAtEnd()) goto success;
        break;
      }

      default: {
      handle_unusual:
        if (tag == 0 ||
            ::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_END_GROUP) {
          goto success;
        }
        DO_(::google::protobuf::internal::WireFormatLite::SkipField(input, tag));
        break;
      }
    }
  }
success:
  // @@protoc_insertion_point(parse_success:ch.sharpsoft.hexapod.transfer.IMU)
  return true;
failure:
  // @@protoc_insertion_point(parse_failure:ch.sharpsoft.hexapod.transfer.IMU)
  return false;
#undef DO_
}

void IMU::SerializeWithCachedSizes(
    ::google::protobuf::io::CodedOutputStream* output) const {
  // @@protoc_insertion_point(serialize_start:ch.sharpsoft.hexapod.transfer.IMU)
  // optional int64 timestamp = 1;
  if (this->timestamp() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(1, this->timestamp(), output);
  }

  // optional float ax = 2;
  if (this->ax() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(2, this->ax(), output);
  }

  // optional float ay = 3;
  if (this->ay() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(3, this->ay(), output);
  }

  // optional float az = 4;
  if (this->az() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(4, this->az(), output);
  }

  // optional float gx = 5;
  if (this->gx() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(5, this->gx(), output);
  }

  // optional float gy = 6;
  if (this->gy() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(6, this->gy(), output);
  }

  // optional float gz = 7;
  if (this->gz() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(7, this->gz(), output);
  }

  // optional float mx = 8;
  if (this->mx() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(8, this->mx(), output);
  }

  // optional float my = 9;
  if (this->my() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(9, this->my(), output);
  }

  // optional float mz = 10;
  if (this->mz() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(10, this->mz(), output);
  }

  // @@protoc_insertion_point(serialize_end:ch.sharpsoft.hexapod.transfer.IMU)
}

::google::protobuf::uint8* IMU::SerializeWithCachedSizesToArray(
    ::google::protobuf::uint8* target) const {
  // @@protoc_insertion_point(serialize_to_array_start:ch.sharpsoft.hexapod.transfer.IMU)
  // optional int64 timestamp = 1;
  if (this->timestamp() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(1, this->timestamp(), target);
  }

  // optional float ax = 2;
  if (this->ax() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(2, this->ax(), target);
  }

  // optional float ay = 3;
  if (this->ay() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(3, this->ay(), target);
  }

  // optional float az = 4;
  if (this->az() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(4, this->az(), target);
  }

  // optional float gx = 5;
  if (this->gx() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(5, this->gx(), target);
  }

  // optional float gy = 6;
  if (this->gy() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(6, this->gy(), target);
  }

  // optional float gz = 7;
  if (this->gz() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(7, this->gz(), target);
  }

  // optional float mx = 8;
  if (this->mx() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(8, this->mx(), target);
  }

  // optional float my = 9;
  if (this->my() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(9, this->my(), target);
  }

  // optional float mz = 10;
  if (this->mz() != 0) {
    target = ::google::protobuf::internal::WireFormatLite::WriteFloatToArray(10, this->mz(), target);
  }

  // @@protoc_insertion_point(serialize_to_array_end:ch.sharpsoft.hexapod.transfer.IMU)
  return target;
}

int IMU::ByteSize() const {
// @@protoc_insertion_point(message_byte_size_start:ch.sharpsoft.hexapod.transfer.IMU)
  int total_size = 0;

  // optional int64 timestamp = 1;
  if (this->timestamp() != 0) {
    total_size += 1 +
      ::google::protobuf::internal::WireFormatLite::Int64Size(
        this->timestamp());
  }

  // optional float ax = 2;
  if (this->ax() != 0) {
    total_size += 1 + 4;
  }

  // optional float ay = 3;
  if (this->ay() != 0) {
    total_size += 1 + 4;
  }

  // optional float az = 4;
  if (this->az() != 0) {
    total_size += 1 + 4;
  }

  // optional float gx = 5;
  if (this->gx() != 0) {
    total_size += 1 + 4;
  }

  // optional float gy = 6;
  if (this->gy() != 0) {
    total_size += 1 + 4;
  }

  // optional float gz = 7;
  if (this->gz() != 0) {
    total_size += 1 + 4;
  }

  // optional float mx = 8;
  if (this->mx() != 0) {
    total_size += 1 + 4;
  }

  // optional float my = 9;
  if (this->my() != 0) {
    total_size += 1 + 4;
  }

  // optional float mz = 10;
  if (this->mz() != 0) {
    total_size += 1 + 4;
  }

  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = total_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void IMU::MergeFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_merge_from_start:ch.sharpsoft.hexapod.transfer.IMU)
  if (GOOGLE_PREDICT_FALSE(&from == this)) MergeFromFail(__LINE__);
  const IMU* source = 
      ::google::protobuf::internal::DynamicCastToGenerated<const IMU>(
          &from);
  if (source == NULL) {
  // @@protoc_insertion_point(generalized_merge_from_cast_fail:ch.sharpsoft.hexapod.transfer.IMU)
    ::google::protobuf::internal::ReflectionOps::Merge(from, this);
  } else {
  // @@protoc_insertion_point(generalized_merge_from_cast_success:ch.sharpsoft.hexapod.transfer.IMU)
    MergeFrom(*source);
  }
}

void IMU::MergeFrom(const IMU& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:ch.sharpsoft.hexapod.transfer.IMU)
  if (GOOGLE_PREDICT_FALSE(&from == this)) MergeFromFail(__LINE__);
  if (from.timestamp() != 0) {
    set_timestamp(from.timestamp());
  }
  if (from.ax() != 0) {
    set_ax(from.ax());
  }
  if (from.ay() != 0) {
    set_ay(from.ay());
  }
  if (from.az() != 0) {
    set_az(from.az());
  }
  if (from.gx() != 0) {
    set_gx(from.gx());
  }
  if (from.gy() != 0) {
    set_gy(from.gy());
  }
  if (from.gz() != 0) {
    set_gz(from.gz());
  }
  if (from.mx() != 0) {
    set_mx(from.mx());
  }
  if (from.my() != 0) {
    set_my(from.my());
  }
  if (from.mz() != 0) {
    set_mz(from.mz());
  }
}

void IMU::CopyFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_copy_from_start:ch.sharpsoft.hexapod.transfer.IMU)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

void IMU::CopyFrom(const IMU& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:ch.sharpsoft.hexapod.transfer.IMU)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

bool IMU::IsInitialized() const {

  return true;
}

void IMU::Swap(IMU* other) {
  if (other == this) return;
  InternalSwap(other);
}
void IMU::InternalSwap(IMU* other) {
  std::swap(timestamp_, other->timestamp_);
  std::swap(ax_, other->ax_);
  std::swap(ay_, other->ay_);
  std::swap(az_, other->az_);
  std::swap(gx_, other->gx_);
  std::swap(gy_, other->gy_);
  std::swap(gz_, other->gz_);
  std::swap(mx_, other->mx_);
  std::swap(my_, other->my_);
  std::swap(mz_, other->mz_);
  _internal_metadata_.Swap(&other->_internal_metadata_);
  std::swap(_cached_size_, other->_cached_size_);
}

::google::protobuf::Metadata IMU::GetMetadata() const {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::Metadata metadata;
  metadata.descriptor = IMU_descriptor_;
  metadata.reflection = IMU_reflection_;
  return metadata;
}

#if PROTOBUF_INLINE_NOT_IN_HEADERS
// IMU

// optional int64 timestamp = 1;
void IMU::clear_timestamp() {
  timestamp_ = GOOGLE_LONGLONG(0);
}
 ::google::protobuf::int64 IMU::timestamp() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.timestamp)
  return timestamp_;
}
 void IMU::set_timestamp(::google::protobuf::int64 value) {
  
  timestamp_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.timestamp)
}

// optional float ax = 2;
void IMU::clear_ax() {
  ax_ = 0;
}
 float IMU::ax() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.ax)
  return ax_;
}
 void IMU::set_ax(float value) {
  
  ax_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.ax)
}

// optional float ay = 3;
void IMU::clear_ay() {
  ay_ = 0;
}
 float IMU::ay() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.ay)
  return ay_;
}
 void IMU::set_ay(float value) {
  
  ay_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.ay)
}

// optional float az = 4;
void IMU::clear_az() {
  az_ = 0;
}
 float IMU::az() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.az)
  return az_;
}
 void IMU::set_az(float value) {
  
  az_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.az)
}

// optional float gx = 5;
void IMU::clear_gx() {
  gx_ = 0;
}
 float IMU::gx() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.gx)
  return gx_;
}
 void IMU::set_gx(float value) {
  
  gx_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.gx)
}

// optional float gy = 6;
void IMU::clear_gy() {
  gy_ = 0;
}
 float IMU::gy() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.gy)
  return gy_;
}
 void IMU::set_gy(float value) {
  
  gy_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.gy)
}

// optional float gz = 7;
void IMU::clear_gz() {
  gz_ = 0;
}
 float IMU::gz() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.gz)
  return gz_;
}
 void IMU::set_gz(float value) {
  
  gz_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.gz)
}

// optional float mx = 8;
void IMU::clear_mx() {
  mx_ = 0;
}
 float IMU::mx() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.mx)
  return mx_;
}
 void IMU::set_mx(float value) {
  
  mx_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.mx)
}

// optional float my = 9;
void IMU::clear_my() {
  my_ = 0;
}
 float IMU::my() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.my)
  return my_;
}
 void IMU::set_my(float value) {
  
  my_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.my)
}

// optional float mz = 10;
void IMU::clear_mz() {
  mz_ = 0;
}
 float IMU::mz() const {
  // @@protoc_insertion_point(field_get:ch.sharpsoft.hexapod.transfer.IMU.mz)
  return mz_;
}
 void IMU::set_mz(float value) {
  
  mz_ = value;
  // @@protoc_insertion_point(field_set:ch.sharpsoft.hexapod.transfer.IMU.mz)
}

#endif  // PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace transfer
}  // namespace hexapod
}  // namespace sharpsoft
}  // namespace ch

// @@protoc_insertion_point(global_scope)

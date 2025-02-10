//package io.nearpay.terminalsdk.utils
//
//sealed class CommonException : Throwable()
//sealed class PosStateErrorException : CommonException()
//sealed class NiceErrorException : CommonException()
//object NotAllowedException : CommonException()
//
///** general **/
//object NotImplementedError : PosStateErrorException()
//object UnexpectedErrorException : NiceErrorException()
//
///** app state **/
//object UnsupportedAppVersionException : ServerException()
//
//object BaseLinFailedException : PosStateErrorException()
//object DevModeEnabledException : PosStateErrorException()
//object InsecureDeviceException : PosStateErrorException()
//object VpnDetectedException : PosStateErrorException()
//object MissingNfcInterfaceException : PosStateErrorException()
//object NfcDisableException : PosStateErrorException()
//object MissingPrinterInterfaceException : PosStateErrorException()
//object MissingLocationPermissionException : PosStateErrorException()
//object LocationDisableException : PosStateErrorException()
//object ConnectivityDownException : PosStateErrorException()
//object UnsupportedSchemeException : PosStateErrorException()
//object MissingHuaweiSafetyDetectApiKey : PosStateErrorException()
//object EncryptionKeysInitializationError : PosStateErrorException()
//
///** network **/
//object HostDownException : NiceErrorException()
//object NetworkException : NiceErrorException()
//object NotFoundException : ServerException()
//
//object ParsingResponseException : NiceErrorException()
//open class ServerException : NiceErrorException()
//
///** session **/
//object NoTerminalFoundException : PosStateErrorException()
//object AuthenticationExpiredException : ServerException()
//
///** terminal **/
//object TerminalDisconnectedException : ServerException()
//
//object TerminalUpdatingException : ServerException()
//
//object TerminalReconcilingException : ServerException()
//
///** spin **/
//object SpinInitializingException : PosStateErrorException()
//object JobActionCalledOnInvalidStateException : NiceErrorException()
//

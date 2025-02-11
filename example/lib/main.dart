// lib/main.dart

import 'package:flutter/material.dart';
import 'package:flutter_terminal_sdk/flutter_terminal_sdk.dart';
import 'package:flutter_terminal_sdk/models/card_reader_callbacks.dart';
import 'package:flutter_terminal_sdk/models/nearpay_user_response.dart';
import 'package:flutter_terminal_sdk/models/purchase_callbacks.dart';
import 'package:flutter_terminal_sdk/models/refund_callbacks.dart';
import 'package:flutter_terminal_sdk/models/terminal_connection_response.dart';
import 'package:flutter_terminal_sdk/models/terminal_response.dart';
import 'package:flutter_terminal_sdk/models/transaction_response.dart' as tr;
import 'package:flutter_terminal_sdk/models/transactions_response.dart';
import 'package:uuid/uuid.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(home: PluginExample());
  }
}

class PluginExample extends StatefulWidget {
  @override
  _PluginExampleState createState() => _PluginExampleState();
}

class _PluginExampleState extends State<PluginExample> {
  final FlutterTerminalSdk _terminalSdk = FlutterTerminalSdk();

  String _status = "Ready";
  String? transactionUuid;

  final TextEditingController _mobileController =
      TextEditingController(text: "+966555555555");
  final TextEditingController _emailController = 
      TextEditingController(text: "user@example.com");
  final TextEditingController _otpController =
      TextEditingController(text: "000000");
  final TextEditingController _amountController =
      TextEditingController(text: "5000");
  final TextEditingController _jwtController =
      TextEditingController(text: "JWT_TOKEN");

  String _selectedScheme = 'VISA';

  NearpayUser? _verifiedUser;

  List<TerminalConnectionModel> _terminals = [];

  TerminalModel? _connectedTerminal;

  /// Initialize the SDK
  Future<void> _initializeSdk() async {
    try {
      setState(() => _status = "Initializing...");
      await _terminalSdk.initialize(
        environment: Environment.sandbox,
        googleCloudProjectNumber: 12345678,  // Set Google Cloud project number
        huaweiSafetyDetectApiKey: "your_api_key",   // Set Huawei API key
        country: Country.sa,
      );
      setState(() => _status = "SDK Initialized");
    } catch (e) {
      setState(() => _status = "Error initializing: $e");
    }
  }

  /// Send Mobile OTP
  Future<void> _sendMobileOtp() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    final mobile = _mobileController.text;
    if (mobile.isEmpty) {
      setState(() => _status = "Please enter a mobile number");
      return;
    }

    try {
      setState(() => _status = "Sending OTP...");
      await _terminalSdk.sendMobileOtp(mobile);
      setState(() => _status = "OTP sent to $mobile");
    } catch (e) {
      setState(() => _status = "Error sending OTP: $e");
    }
  }

  /// Verify Mobile OTP
  Future<void> _verifyMobileOtp() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    final mobile = _mobileController.text.trim();
    final code = _otpController.text.trim();
    if (mobile.isEmpty || code.isEmpty) {
      setState(() => _status = "Please enter mobile number AND OTP code");
      return;
    }

    try {
      setState(() => _status = "Verifying OTP...");
      final user = await _terminalSdk.verifyMobileOtp(
        mobileNumber: mobile,
        code: code,
      );
      setState(() {
        _verifiedUser = user;
        _status = "OTP verified! User: ${user.name}";
      });
    } catch (e) {
      setState(() => _status = "Error verifying OTP: $e");
    }
  }

  /// Verify Mobile OTP
  Future<void> _verifyJWT() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    final jwt = _jwtController.text.trim();
    if (jwt.isEmpty) {
      setState(() => _status = "Please enter jwt");
      return;
    }

    try {
      setState(() => _status = "Verifying jwt...");
      final terminalModel = await _terminalSdk.jwtLogin(
        jwt: jwt,
      );
      setState(() {
        _connectedTerminal = terminalModel;
        _status = "jwt verified! terminal: ${terminalModel.tid}";
      });
    } catch (e) {
      setState(() => _status = "Error verifying OTP: $e");
    }
  }

  /// Send Email OTP
  Future<void> _sendEmailOtp() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    final email = _emailController.text;
    if (email.isEmpty) {
      setState(() => _status = "Please enter an email address");
      return;
    }

    try {
      setState(() => _status = "Sending OTP...");
      await _terminalSdk.sendEmailOtp(email);
      setState(() => _status = "OTP sent to $email");
    } catch (e) {
      setState(() => _status = "Error sending OTP: $e");
    }
  }

  /// Verify Mobile OTP
  Future<void> _verifyEmailOtp() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    final email = _emailController.text.trim();
    final code = _otpController.text.trim();
    if (email.isEmpty || code.isEmpty) {
      setState(() => _status = "Please enter email address AND OTP code");
      return;
    }

    try {
      setState(() => _status = "Verifying OTP...");
      final user = await _terminalSdk.verifyEmailOtp(
        email: email,
        code: code,
      );
      setState(() {
        _verifiedUser = user;
        _status = "OTP verified! User: ${user.name}";
      });
    } catch (e) {
      setState(() => _status = "Error verifying OTP: $e");
    }
  }

  /// get User
  Future<void> _getUser() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "getting user...");
      final user = await _terminalSdk.getUser(
        uuid: _verifiedUser!.userUUID!,
      );
      setState(() {
        _verifiedUser = user;
        _status = "User: ${user.name} ${user.email} ${user.mobile}";
      });
    } catch (e) {
      setState(() => _status = "$e");
    }
  }

  /// logout
  Future<void> _logout() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "logging out...");
      final message = await _terminalSdk.logout(
        uuid: _verifiedUser!.userUUID!,
      );
      setState(() {
        _verifiedUser = null;
        _status = message;
      });
    } catch (e) {
      setState(() => _status = "$e");
    }
  }

  /// Get Terminals for the verified user
  Future<void> _getTerminals() async {
    if (_verifiedUser == null) {
      setState(() => _status = "No verified user yet");
      return;
    }
    try {
      setState(() => _status = "Fetching terminals...");
      final fetchedTerminals = await _terminalSdk.getTerminalList(
        _verifiedUser!.userUUID!,
      );
      setState(() {
        _terminals = fetchedTerminals;
        _status = "Fetched ${_terminals.length} terminals";
      });
    } catch (e) {
      setState(() => _status = "Error fetching terminals: $e");
    }
  }

  /// Get Terminals for the verified user
  Future<void> _getTerminal() async {
    try {
      setState(() => _status = "get Terminal 0211301800113018...");
      final fetchedTerminal = await _terminalSdk.getTerminal(
        tid: '0211301800113018',
      );

      setState(() {
        _connectedTerminal = fetchedTerminal;
        _status =
            "Fetched terminal 0211301800113018 = ${_connectedTerminal?.isReady}";
      });
    } catch (e) {
      setState(() => _status = "Error fetching terminal: $e");
    }
  }

  /// Connect to a specific terminal
  Future<void> _connectToTerminal(TerminalConnectionModel terminal) async {
    if (_verifiedUser == null) {
      setState(() => _status = "No verified user yet");
      return;
    }

    try {
      setState(() => _status =
          "Connecting to terminal ${terminal.name ?? terminal.tid}...");

      final connectedTerminal = await _terminalSdk.connectTerminal(
        tid: terminal.tid,
        userUUID: terminal.userUUID,
        terminalUUID: terminal.uuid,
      );
      setState(() {
        _connectedTerminal = connectedTerminal;
        _status =
            "Connected to TID: ${connectedTerminal.tid}, isReady: ${connectedTerminal.isReady}";
      });
    } catch (e) {
      setState(() => _status = "Error connecting: $e");
    }
  }

  /// Example purchase
  Future<void> _purchase() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    if (_connectedTerminal == null) {
      setState(() => _status =
          "No connected terminal. Please connect to a terminal first.");
      return;
    }

    final amountText = _amountController.text.trim();
    if (amountText.isEmpty) {
      setState(() => _status = "Please enter an amount.");
      return;
    }

    final amount = int.tryParse(amountText);
    if (amount == null) {
      setState(() => _status = "Invalid amount entered.");
      return;
    }

    final scheme = _selectedScheme;

    try {
      setState(() => _status = "Purchasing...");

      // Define the callbacks for purchase events
      final callbacks = PurchaseCallbacks(
        onReadingStarted: () {
          setState(() => _status = "Reading started...");
        },
        onReaderWaiting: () {
          setState(() => _status = "Reader waiting...");
        },
        onReaderReading: () {
          setState(() => _status = "Reader reading...");
        },
        onReaderRetry: () {
          setState(() => _status = "Reader retrying...");
        },
        onPinEntering: () {
          setState(() => _status = "Entering PIN...");
        },
        onReaderFinished: () {
          setState(() => _status = "Reader finished.");
        },
        onReaderError: (message) {
          setState(() => _status = "Reader error: $message");
        },
        onCardReadSuccess: () {
          setState(() => _status = "Card read successfully.");
        },
        onCardReadFailure: (message) {
          setState(() => _status = "Card read failure: $message");
        },
        onSendTransactionSuccess: () {
          setState(() => _status = "Transaction sent successfully.");
        },
        onSendTransactionFailure: (message) {
          setState(() => _status = "Transaction failed: $message");
        },
        onSendTransactionSuccessData: (tr.TransactionResponse response) {
          setState(() => _status = "Purchase Successful!");
          _showTransactionDialog(response);
        },
      );
      transactionUuid = const Uuid().v4();

      final transactionResposne = await _connectedTerminal!.purchase(
        amount: amount,
        scheme: scheme,
        callbacks: callbacks,
        transactionUuid: transactionUuid!!,
      );

      print("Transaction Resposne: $transactionResposne");
    } catch (e) {
      setState(() => _status = "Error in purchase: $e");
    }
  }

  void _showTransactionDialog(tr.TransactionResponse response) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Transaction Successful"),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                Text("ID: ${response.id}"),
                Text("Action Code: ${response.actionCode}"),
                Text("Type: ${response.type}"),
                Text("Status: ${response.status.toShortString()}"),
                Text("Amount: ${response.amount} ${response.currency}"),
                Text("Created At: ${response.createdAt ?? 'N/A'}"),
                Text("Completed At: ${response.completedAt ?? 'N/A'}"),
                // Add more fields as needed
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  void _showTransactionListDialog(TransactionsResponse response) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Transaction Successful"),
          content: SingleChildScrollView(
            child: ListBody(
              children: response.data.map((e) {
                return Container(
                  margin: const EdgeInsets.only(bottom: 10),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.grey),
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: Column(
                    children: [
                      Text("ID: ${e.uuid}"),
                      Text("Scheme: ${e.scheme}"),
                      Text(
                          "Customer Reference Number: ${e.customerReferenceNumber}"),
                      Text("PAN: ${e.pan}"),
                      Text("Amount Authorized: ${e.amountAuthorized}"),
                      Text("Transaction Type: ${e.transactionType}"),
                      Text("Currency: ${e.currency}"),
                      Text("Is Approved: ${e.isApproved}"),
                      Text("Is Reversed: ${e.isReversed}"),
                      Text("Is Reconciled: ${e.isReconciled}"),
                      Text("Start Date: ${e.startDate}"),
                      Text("Start Time: ${e.startTime}"),
                      Text("Performance: ${e.performance}"),
                    ],
                  ),
                );
              }).toList(),
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  /// Example refund
  Future<void> _refund() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    // Ensure that you have a valid transactionUuid. This example uses a placeholder.
    if (transactionUuid == null) {
      setState(() => _status = "No transaction to refund.");
      return;
    }
    final amountText = _amountController.text.trim();
    if (amountText.isEmpty) {
      setState(() => _status = "Please enter an amount for refund.");
      return;
    }

    final amount = int.tryParse(amountText);
    if (amount == null) {
      setState(() => _status = "Invalid amount entered.");
      return;
    }

    final scheme = _selectedScheme;

    var callback = RefundCallbacks(
      cardReaderCallbacks: CardReaderCallbacks(
        onReadingStarted: () {
          setState(() => _status = "Reading started...");
        },
        onReaderWaiting: () {
          setState(() => _status = "Reader waiting...");
        },
        onReaderReading: () {
          setState(() => _status = "Reader reading...");
        },
        onReaderRetry: () {
          setState(() => _status = "Reader retrying...");
        },
        onPinEntering: () {
          setState(() => _status = "Entering PIN...");
        },
        onReaderFinished: () {
          setState(() => _status = "Reader finished.");
        },
        onReaderError: (message) {
          setState(() => _status = "Reader error: $message");
        },
        onCardReadSuccess: () {
          setState(() => _status = "Card read successfully.");
        },
        onCardReadFailure: (message) {
          setState(() => _status = "Card read failure: $message");
        },
      ),
      onSendTransactionSuccess: (response) {
        setState(() => _status = "Refund Successful!");
        _showTransactionDialog(response);
      },
      onSendTransactionFailure: (message) {
        setState(() => _status = "Refund failed: $message");
      },
    );
    var refundUuid = const Uuid().v4();

    try {
      setState(() => _status = "Refunding...");
      final result = await _connectedTerminal?.refund(
        refundUuid: refundUuid,
        transactionUuid: transactionUuid!,
        amount: amount,
        scheme: null,
        //todo
        callbacks: callback,
      );
      setState(() => _status = "Refund Successful: ${result.toString()}");
    } catch (e) {
      setState(() => _status = "Error in refund: $e");
    }
  }

  /// Example refund
  Future<void> _getTransactionDetails() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    // Ensure that you have a valid transactionUuid. This example uses a placeholder.
    // if (transactionUuid == null) {
    //   setState(() => _status = "No transaction to get details.");
    //   return;
    // }

    try {
      setState(() => _status = "GetTransaction...");
      final result = await _connectedTerminal?.getTransactionDetails(
        transactionUuid: "068cf836-2c84-4db6-81cb-6691024f83bd",
      );

      setState(() =>
          _status = "GetTransaction Successful: ${result?.receipts[0].id}");
    } catch (e) {
      setState(() => _status = "Error in getTransaction: $e");
    }
  }

  /// Example refund
  Future<void> _getTransactionList() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "GetTransactionList...");
      final result = await _connectedTerminal?.getTransactionList(
        page: 1,
        pageSize: 10,
      );
      _showTransactionListDialog(result!);

      setState(
          () => _status = "GetTransaction Successful: ${result.toString()}");
    } catch (e) {
      setState(() => _status = "Error in getTransaction: $e");
    }
  }

  /// get Reconciliation list
  Future<void> _getReconciliationList() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "GetReconciliationList...");
      final result = await _connectedTerminal?.getReconcileList(
        page: 1,
        pageSize: 10,
      );
      setState(
          () => _status = "GetReconciliation Successful: ${result.toString()}");
    } catch (e) {
      setState(() => _status = "Error in getReconciliation: $e");
    }
  }

  /// get Reconcile details
  Future<void> _getReconcileDetails() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "GetReconciliation...");
      final result = await _connectedTerminal?.getReconcileDetails(
          uuid: transactionUuid ?? "");
      setState(
          () => _status = "GetReconciliation Successful: ${result.toString()}");
    } catch (e) {
      setState(() => _status = "Error in GetReconciliation: $e");
    }
  }

  /// reconcile
  Future<void> _reconcile() async {
    if (!_terminalSdk.isInitialized) {
      setState(() => _status = "SDK not initialized yet");
      return;
    }

    try {
      setState(() => _status = "Reconcile...");
      final result = await _connectedTerminal?.reconcile();
      setState(() => _status = "Reconcile Successful: ${result.toString()}");
    } catch (e) {
      setState(() => _status = "Error in Reconcile: $e");
    }
  }

  /// Builds a list view of available terminals
  Widget _buildTerminalsList() {
    if (_terminals.isEmpty) {
      return const Text("No terminals fetched yet.");
    }

    return ListView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(), // Prevent inner scrolling
      itemCount: _terminals.length,
      itemBuilder: (context, index) {
        final terminal = _terminals[index];
        return Card(
          child: ListTile(
            title: Text(terminal.name ?? "Unnamed Terminal"),
            subtitle: Text("TID: ${terminal.tid}"),
            onTap: () => _connectToTerminal(terminal),
          ),
        );
      },
    );
  }

  @override
  void dispose() {
    _terminalSdk.dispose();
    _mobileController.dispose();
    _emailController.dispose();
    _otpController.dispose();
    _amountController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Flutter Terminal SDK Example"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Expanded(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text("Status: $_status", style: const TextStyle(fontSize: 16)),
              const SizedBox(height: 20),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      ElevatedButton(
                        onPressed: _initializeSdk,
                        child: const Text("Initialize SDK"),
                      ),
                      const SizedBox(height: 20),
                      TextField(
                        controller: _mobileController,
                        decoration: const InputDecoration(
                          labelText: "Mobile Number",
                          hintText: "+966xxxxxxxxx",
                        ),
                      ),
                      ElevatedButton(
                        onPressed: _sendMobileOtp,
                        child: const Text("Send Mobile OTP"),
                      ),
                      TextField(
                        controller: _emailController,
                        decoration: const InputDecoration(
                          labelText: "Email Address",
                          hintText: "email@x.com",
                        ),
                      ),
                      ElevatedButton(
                        onPressed: _sendEmailOtp,
                        child: const Text("Send Email OTP"),
                      ),
                      const SizedBox(height: 20),
                      TextField(
                        controller: _otpController,
                        decoration: const InputDecoration(
                          labelText: "OTP Code",
                          hintText: "1234",
                        ),
                      ),
                      ElevatedButton(
                        onPressed: _verifyMobileOtp,
                        child: const Text("Verify Mobile OTP"),
                      ),
                      ElevatedButton(
                        onPressed: _verifyEmailOtp,
                        child: const Text("Verify Email OTP"),
                      ),
                      const SizedBox(height: 20),
                      TextField(
                        controller: _jwtController,
                        decoration: const InputDecoration(
                          labelText: "jwt",
                          hintText: "342FDSF234WEFSADF",
                        ),
                      ),
                      ElevatedButton(
                        onPressed: _verifyJWT,
                        child: const Text("Verify JWT"),
                      ),
                      ElevatedButton(
                        onPressed: _getTerminal,
                        child: const Text("Get Terminal 0211301800113018"),
                      ),
                      const SizedBox(height: 20),
                      // Only show "Get Terminals" if we have a verified user
                      if (_verifiedUser != null) ...[
                        ElevatedButton(
                          onPressed: _getUser,
                          child: const Text("Get User"),
                        ),
                        ElevatedButton(
                          onPressed: _logout,
                          child: const Text("logout"),
                        ),
                        ElevatedButton(
                          onPressed: _getTerminals,
                          child: const Text("Get Terminals"),
                        ),
                        const SizedBox(height: 20),
                        // Display the terminals list
                        _buildTerminalsList(),
                        const SizedBox(height: 20),
                      ],

                      // Purchase inputs
                      TextField(
                        controller: _amountController,
                        keyboardType: TextInputType.number, // Correct placement
                        decoration: const InputDecoration(
                          labelText: "Amount",
                          hintText: "5000",
                        ),
                      ),
                      const SizedBox(height: 10),
                      DropdownButtonFormField<String>(
                        decoration: const InputDecoration(
                          labelText: "Payment Scheme",
                        ),
                        value: _selectedScheme,
                        items: <String>['VISA', 'MASTERCARD', 'AMEX', 'TROY']
                            .map((String value) {
                          return DropdownMenuItem<String>(
                            value: value,
                            child: Text(value),
                          );
                        }).toList(),
                        onChanged: (newValue) {
                          setState(() {
                            _selectedScheme = newValue!;
                          });
                        },
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _purchase,
                        child: const Text("Purchase"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _refund,
                        child: const Text("Refund"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _getTransactionDetails,
                        child: const Text("Get Transaction Details"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _getTransactionList,
                        child: const Text("Get Transaction List"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _getReconcileDetails,
                        child: const Text("Get Reconciliation List"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _getReconcileDetails,
                        child: const Text("Get Reconciliation Details"),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _reconcile,
                        child: const Text("Reconcile"),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

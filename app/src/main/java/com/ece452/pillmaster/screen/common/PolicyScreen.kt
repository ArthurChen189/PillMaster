package com.ece452.pillmaster.screen.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.ece452.pillmaster.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(
    navController: NavController,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = "PRIVACY POLICY - PILLMASTER PROJECT",
            style = Typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "Version 1.0 of July 24, 2023",
            style = Typography.bodyLarge.copy(color = Grey50),
            modifier = Modifier.padding(all = 16.dp)
        )

        // Reference of the privacy policy: https://www.medisafe.com/
        Text(
            "We have the highest regard for your privacy and personal information and realize that the success of our services depends on the trust that you have in the way we handle your personal information. By entrusting us with your information, we would like to assure you of our commitment to keep such information private. We have taken considerable steps to protect the confidentiality, security and integrity of this information. We encourage you to review the following information carefully.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            "This policy sets out our commitments and explains the rights that you have with respect to your personal information. If you do not agree to the terms of this Privacy Policy, please do not use the Service.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "WHAT INFORMATION DO WE COLLECT",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "Personal Health Information – you may choose to use certain features of the Service that will allow you to input other Personal Information with respect to your health, such as the medications you take, the date of your prescriptions, how often you take your medication, dosage, email address of you and your caregivers.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "HOW DO WE USE THE INFORMATION WE COLLECT",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "Provision of service – we use the Personal Health Information you provide us for the provision and improvement of our Service to you, and provide information that you request form us. For example, data collected automatically on the Service may be used to help diagnose problems with our servers, to make our Service more useful, to customize it and personalize its content for you (for example, we will use your Health information to send you reminders to take your medications).",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "WITH WHOM DO WE SHARE YOUR PERSONAL INFORMATION",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "Service providers – we may share your Personal Health Information, as is reasonably necessary, with our service providers, including vendors and suppliers that provide us with development services, technology (such as Firebase), services, or content for the operation, development and maintenance of our Service or data and analysis on Service use, who are bound by an obligation of confidentiality, provided that we will only share Personal Health Information to the extent necessary with such service providers.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "HOW WE PROTECT YOUR INFORMATION",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "We have implemented administrative, technical, and physical safeguards to help prevent unauthorized access, use, or disclosure of your Personal Information. Your information is stored on secure servers and isn’t publicly available. We limit access of your information only to those employees or partners that need to know the information in order to enable the carrying out of the agreement between us.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            "While we seek to protect your information to ensure that it is kept confidential, no security system is infallible and impervious, and we cannot absolutely guarantee its security. You should be aware that there is always some risk involved in transmitting information over the internet. While we strive to protect your Personal Health Information, we cannot ensure or warrant the security and privacy of your Personal Health Information or other content you transmit using the Service, and you do so at your own risk.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "CHILDREN",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "Our Service is intended for use by persons over the age of majority (as determined by applicable laws where such persons reside in: “Age of Majority”), unless we are provided with a valid parental or guardianship approval and consent, in accordance with the requirements of applicable laws. Under no circumstances should the Service be used by persons under the Age of Majority. We will not knowingly collect Personal Information from any person under the Age of Majority unless as described herein, and at our sole discretion.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            "If you discover that a child has been using the Service without your consent, or that someone has been using the Service for or on behalf of your child without your consent, please contact us using the information below under “How to Contact Us” and we will take reasonable steps to delete the child’s information from our active databases.",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Text(
            text = "HOW TO CONTACT US",
            style = Typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            "If you have any questions, comments, requests, or concerns related to this Privacy Policy or the privacy practices of our Service, please contact us through email at: anna.zhang@uwaterloo.ca",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }
}

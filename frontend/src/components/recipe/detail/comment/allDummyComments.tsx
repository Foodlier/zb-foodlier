import Comment from './Comment'

// 댓글 및 대댓글의 더미 데이터 정의
const dummyComments: Comment[] = [
  {
    commentId: '1',
    message:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
    createdAt: '2023-10-05',
    isDeleted: false,
    nickname: '사용자1',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '-1', // 최상위 댓글
    isReply: false, // 댓글
  },
  {
    commentId: '2',
    message:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galleyLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
    createdAt: '2023-10-06',
    isDeleted: false,
    nickname: '사용자2',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '-1', // 최상위 댓글
    isReply: false, // 댓글
  },
  {
    commentId: '3',
    message:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. ',
    createdAt: '2023-10-07',
    isDeleted: false,
    nickname: '사용자3',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '1', // 첫 번째 댓글에 대한 대댓글
    isReply: false, // 대댓글
  },
  {
    commentId: '4',
    message:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. ',
    createdAt: '2023-10-08',
    isDeleted: false,
    nickname: '사용자4',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '1', // 첫 번째 댓글에 대한 대댓글
    isReply: false, // 대댓글
  },
]

// 대댓글 데이터 구성
const dummyReplies: Comment[] = [
  {
    commentId: '7',
    message:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galleyLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
    createdAt: '2023-10-13',
    isDeleted: false,
    nickname: '사용자7',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '5', // 다섯 번째 댓글에 대한 대댓글
    isReply: true, // 대댓글
  },
  {
    commentId: '8',
    message: '대댓글 5-2',
    createdAt: '2023-10-14',
    isDeleted: false,
    nickname: '사용자8',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
    referenceId: '5', // 다섯 번째 댓글에 대한 대댓글
    isReply: true, // 대댓글
  },
  //   //   {
  //   //     commentId: '9',
  //   //     message:
  //   //       'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galleyLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
  //   //     createdAt: '2023-10-15',
  //   //     isDeleted: false,
  //   //     nickname: '사용자9',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '10',
  //   //     message:
  //   //       '대댓글 Lorem Ipsum is simply dummy text of the printing and typesetting industry.text ever since the 1500s, when an unknown printer took a galley',
  //   //     createdAt: '2023-10-16',
  //   //     isDeleted: false,
  //   //     nickname: '사용자10',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '11',
  //   //     message: '대댓글 6-3',
  //   //     createdAt: '2023-10-17',
  //   //     isDeleted: false,
  //   //     nickname: '사용자11',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '12',
  //   //     message: '대댓글 6-4',
  //   //     createdAt: '2023-10-18',
  //   //     isDeleted: false,
  //   //     nickname: '사용자12',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '13',
  //   //     message: '대댓글 6-5',
  //   //     createdAt: '2023-10-19',
  //   //     isDeleted: false,
  //   //     nickname: '사용자13',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '14',
  //   //     message: '대댓글 6-6',
  //   //     createdAt: '2023-10-20',
  //   //     isDeleted: false,
  //   //     nickname: '사용자14',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '6', // 여섯 번째 댓글에 대한 대댓글
  //   //     isReply: true, // 대댓글
  //   //   },
  //   //   {
  //   //     commentId: '15',
  //   //     message: '첫 번째 댓글',
  //   //     createdAt: '2023-10-05',
  //   //     isDeleted: false,
  //   //     nickname: '사용자1',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '-1', // 최상위 댓글
  //   //     isReply: false, // 댓글
  //   //   },
  //   //   {
  //   //     commentId: '16',
  //   //     message: '두 번째 댓글',
  //   //     createdAt: '2023-10-06',
  //   //     isDeleted: false,
  //   //     nickname: '사용자2',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '-1', // 최상위 댓글
  //   //     isReply: false, // 댓글
  //   //   },
  //   //   {
  //   //     commentId: '17',
  //   //     message: '첫 번째 댓글',
  //   //     createdAt: '2023-10-05',
  //   //     isDeleted: false,
  //   //     nickname: '사용자1',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '-1', // 최상위 댓글
  //   //     isReply: false, // 댓글
  //   //   },
  //   //   {
  //   //     commentId: '18',
  //   //     message: '두 번째 댓글',
  //   //     createdAt: '2023-10-06',
  //   //     isDeleted: false,
  //   //     nickname: '사용자2',
  //   //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //   //     referenceId: '-1', // 최상위 댓글
  //   //     isReply: false, // 댓글
  //   //   },
]

const allDummyComments: Comment[] = dummyComments.concat(dummyReplies)

export default allDummyComments
// export default dummyComments

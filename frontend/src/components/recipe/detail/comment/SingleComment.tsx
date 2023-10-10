import React, { useState } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/SingleComment.styled'
import useIcon from '../../../../hooks/useIcon'
import { palette } from '../../../../constants/Styles'

interface Comment {
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
}

interface SingleCommentProps {
  comments: Comment[]
}

function SingleComment({ comments }: SingleCommentProps) {
  const { ExpandUpLight, ExpandDownLight } = useIcon()
  const [iconExpanded, setIconExpanded] = useState(false)
  const [openReply, setOpenReply] = useState(false)
  const [commentValue, setCommentValue] = useState('')

  const handleToggleReply = () => {
    setOpenReply(!openReply)
    setIconExpanded(!iconExpanded)
  }

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.target.value)
  }

  const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      const newComment: Comment = {
        message: commentValue,
        createdAt: '현재 날짜 및 시간',
        isDeleted: false,
        nickname: '현재 사용자',
        profileUrl: 'https://source.unsplash.com/random/50x50/?person',
      }

      const updatedComments = [...comments, newComment]

      setCommentValue('')

      console.log('답글이 추가되었습니다:', newComment)
      console.log('업데이트된 댓글 목록:', updatedComments)
    } catch (error) {
      console.error('API 호출 오류:', error)
    }

    setOpenReply(false)
  }

  return (
    <>
      {/* Comment Lists  */}
      {comments.map(comment => (
        <div key={comment.message}>
          <S.CommentItemWrapper>
            <S.CommentHeader>
              <S.CommentUserInfo>
                <S.UserImg src={comment.profileUrl} alt={comment.nickname} />
                <S.UserNickname>{comment.nickname}</S.UserNickname>
              </S.CommentUserInfo>
              <S.CommentDate>{comment.createdAt}</S.CommentDate>
            </S.CommentHeader>
            <S.CommentContent>{comment.message}</S.CommentContent>
          </S.CommentItemWrapper>
        </div>
      ))}
      {/* <Comment /> */}
      <S.ReplyTo onClick={handleToggleReply}>
        {iconExpanded ? (
          <ExpandUpLight size={3} color={palette.textPrimary} />
        ) : (
          <ExpandDownLight size={3} color={palette.textPrimary} />
        )}
        댓글 1개
      </S.ReplyTo>

      {/* Root Comment Form */}
      {openReply && (
        <S.CommentForm onSubmit={onSubmit}>
          <S.CommentInput
            type="text"
            placeholder="댓글을 입력하세요"
            onChange={handleChange}
            value={commentValue}
          />
          <S.CommentSubmit type="submit">등록</S.CommentSubmit>
        </S.CommentForm>
      )}
    </>
  )
}

export default SingleComment

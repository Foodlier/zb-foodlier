import React from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeCommentList.styled'

interface Comment {
  message: string
  createdAt: string
  isDeleted: boolean
  nickname: string
  profileUrl: string
}

interface RecipeCommentProps {
  comments: Comment[] // 댓글 목록을 props로 받아온다고 가정합니다.
}

function RecipeComment({ comments }: RecipeCommentProps) {
  return (
    <S.CommentContainer>
      {/* Root Comment Form */}
      <S.CommentForm>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value=""
        />
        <S.CommentSubmit type="button">등록</S.CommentSubmit>
      </S.CommentForm>
      {/* <S.CommentHeader>
        <S.CommentUserInfo>
          <S.UserImg src={comment.profileUrl} alt={comment.nickname} />
          <S.UserNickname>{comment.nickname}</S.UserNickname>
        </S.CommentUserInfo>
        <S.CommentDate>{formattedCreatedAt}</S.CommentDate>
      </S.CommentHeader> */}
      {comments.map(comment => (
        <div key={comment.message}>
          <S.UserNickname>{comment.nickname}</S.UserNickname>
          <S.CommentContent>{comment.message}</S.CommentContent>
          <S.CommentDate>{comment.createdAt}</S.CommentDate>
          <S.UserImg src={comment.profileUrl} alt={comment.nickname} />
        </div>
      ))}
    </S.CommentContainer>
  )
}

export default RecipeComment
